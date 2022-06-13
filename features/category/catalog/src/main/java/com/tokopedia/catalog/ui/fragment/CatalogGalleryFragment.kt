package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.gallery.CatalogBottomGalleyRecyclerViewAdapter
import com.tokopedia.catalog.adapter.gallery.CatalogGalleryImagePagerAdapter
import com.tokopedia.catalog.analytics.CatalogDetailAnalytics
import com.tokopedia.catalog.model.raw.CatalogImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.user.session.UserSession
import kotlinx.android.synthetic.main.fragment_catalog_gallery.*

class CatalogGalleryFragment : Fragment() {
    private var catalogImages: ArrayList<CatalogImage>? = null
    private var currentImage: Int = -1
    private var catalogName : String = ""
    private var catalogId : String = ""
    private lateinit var catalogBottomGalleyRecyclerViewAdapter: CatalogBottomGalleyRecyclerViewAdapter
    private var showBottomGallery = false
    private var showBottomNumbering = false
    private var reviewId : String = ""

    companion object {
        private const val ARG_CATALOG_NAME = "ARG_CATALOG_NAME"
        private const val ARG_CATALOG_ID = "ARG_CATALOG_ID"
        private const val ARG_EXTRA_IMAGES = "ARG_EXTRA_IMAGES"
        private const val ARG_EXTRA_CURRENT_IMAGE = "ARG_EXTRA_CURRENT_IMAGE"
        private const val ARG_SHOW_BOTTOM_GALLERY = "ARG_SHOW_BOTTOM_GALLERY"
        private const val ARG_SHOW_BOTTOM_NUMBERING = "ARG_SHOW_BOTTOM_NUMBERING"
        private const val ARG_REVIEW_ID = "ARG_REVIEW_ID"

        fun newInstance(catalogName : String,catalogId : String,currentItem: Int, catalogImages: ArrayList<CatalogImage>,
                        showBottomGallery : Boolean = false,
                        showNumbering : Boolean = false,
                        reviewId : String = "" ): CatalogGalleryFragment {
            val fragment = CatalogGalleryFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_EXTRA_IMAGES, catalogImages)
            bundle.putString(ARG_CATALOG_NAME, catalogName)
            bundle.putString(ARG_CATALOG_ID, catalogId)
            bundle.putInt(ARG_EXTRA_CURRENT_IMAGE, currentItem)
            bundle.putBoolean(ARG_SHOW_BOTTOM_GALLERY,showBottomGallery)
            bundle.putBoolean(ARG_SHOW_BOTTOM_NUMBERING,showNumbering)
            bundle.putString(ARG_REVIEW_ID, reviewId)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_catalog_gallery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            catalogImages = requireArguments().getParcelableArrayList(ARG_EXTRA_IMAGES)
            currentImage = requireArguments().getInt(ARG_EXTRA_CURRENT_IMAGE)
            catalogName = requireArguments().getString(ARG_CATALOG_NAME) ?: ""
            catalogId = requireArguments().getString(ARG_CATALOG_ID) ?: ""
            showBottomGallery = requireArguments().getBoolean(ARG_SHOW_BOTTOM_GALLERY)
            showBottomNumbering = requireArguments().getBoolean(ARG_SHOW_BOTTOM_NUMBERING)
            reviewId = requireArguments().getString(ARG_REVIEW_ID) ?: ""

        }

        if (catalogImages != null) {
            val catalogImageAdapter = CatalogGalleryImagePagerAdapter(catalogImages!!, null)
            var previousPosition: Int = -1
            view_pager_intermediary.adapter = catalogImageAdapter
            view_pager_intermediary.setCurrentItem(currentImage, true)
            view_pager_intermediary.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(p0: Int) {

                }

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

                }

                override fun onPageSelected(position: Int) {
                    previousPosition = position
                    if(showBottomGallery){
                        catalogBottomGalleyRecyclerViewAdapter.changeSelectedPosition(position)
                        catalogBottomGalleyRecyclerViewAdapter.notifyDataSetChanged()
                    }
                    if(showBottomNumbering){
                        numbering.text = (context?.resources?.getString(com.tokopedia.catalog.R.string.catalog_numbering_images, (position + 1).toString(), catalogImages?.size.toString()) ?: "")
                    }
                }

            })
            if(showBottomGallery){
                image_recycler_view.show()
                image_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                catalogBottomGalleyRecyclerViewAdapter = CatalogBottomGalleyRecyclerViewAdapter(catalogImages!!, onImageClick, currentImage)
                image_recycler_view.adapter = catalogBottomGalleyRecyclerViewAdapter
            }
            if(showBottomNumbering){
                numbering.text = (context?.resources?.getString(com.tokopedia.catalog.R.string.catalog_numbering_images, (currentImage + 1).toString(), catalogImages?.size.toString()) ?: "")
                numbering.show()
            }
        }

        cross.setOnClickListener {
            if(showBottomNumbering){
                CatalogDetailAnalytics.sendEvent(
                    CatalogDetailAnalytics.EventKeys.EVENT_NAME_CLICK_PG,
                    CatalogDetailAnalytics.CategoryKeys.PAGE_EVENT_CATEGORY,
                    CatalogDetailAnalytics.ActionKeys.CLICK_CLOSE_ON_IMAGE_REVIEW,
                    "$catalogName - $catalogId - $reviewId",UserSession(context).userId,catalogId)
            }
            activity?.finish()
        }
    }

    private val onImageClick : (Int) -> Unit = {adapterPosition ->
        view_pager_intermediary.setCurrentItem(adapterPosition, true)
        catalogBottomGalleyRecyclerViewAdapter.changeSelectedPosition(adapterPosition)
        catalogBottomGalleyRecyclerViewAdapter.notifyDataSetChanged()
    }
}