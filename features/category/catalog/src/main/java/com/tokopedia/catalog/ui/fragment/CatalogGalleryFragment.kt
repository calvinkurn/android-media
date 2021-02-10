package com.tokopedia.catalog.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.catalog.R
import com.tokopedia.catalog.adapter.gallery.CatalogBottomGalleyRecyclerViewAdapter
import com.tokopedia.catalog.adapter.gallery.CatalogGalleryImagePagerAdapter
import com.tokopedia.catalog.analytics.CatalogDetailPageAnalytics
import com.tokopedia.catalog.model.raw.CatalogImage
import kotlinx.android.synthetic.main.fragment_catalog_gallery.*

class CatalogGalleryFragment : Fragment(), CatalogBottomGalleyRecyclerViewAdapter.Listener {
    private var catalogImages: ArrayList<CatalogImage>? = null
    private var currentImage: Int = -1
    private lateinit var catalogBottomGalleyRecyclerViewAdapter: CatalogBottomGalleyRecyclerViewAdapter

    companion object {
        private const val ARG_EXTRA_IMAGES = "ARG_EXTRA_IMAGES"
        private const val ARG_EXTRA_CURRENT_IMAGE = "ARG_EXTRA_CURRENT_IMAGE"
        private const val LEFT = "left"
        private const val RIGHT = "right"

        private const val SHOW_BOTTOM_GALLERY = false

        fun newInstance(currentItem: Int, catalogImages: ArrayList<CatalogImage>): CatalogGalleryFragment {
            val fragment = CatalogGalleryFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_EXTRA_IMAGES, catalogImages)
            bundle.putInt(ARG_EXTRA_CURRENT_IMAGE, currentItem)
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
            catalogImages = arguments!!.getParcelableArrayList(ARG_EXTRA_IMAGES)
            currentImage = arguments!!.getInt(ARG_EXTRA_CURRENT_IMAGE)
        }

        if (catalogImages != null) {
            val catalogImageAdapter = CatalogGalleryImagePagerAdapter(catalogImages!!, null)
            var previousPosition: Int = -1
            view_pager_intermediary.adapter = catalogImageAdapter
            view_pager_intermediary.setCurrentItem(currentImage, true)
            view_pager_intermediary.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(p0: Int) {}

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(position: Int) {
                    when {
                        previousPosition >= 0 -> when {
                            previousPosition > position -> CatalogDetailPageAnalytics.trackEventSwipeIndexPicture(LEFT)
                            previousPosition < position -> CatalogDetailPageAnalytics.trackEventSwipeIndexPicture(RIGHT)
                        }
                    }
                    previousPosition = position
                    if(SHOW_BOTTOM_GALLERY){
                        catalogBottomGalleyRecyclerViewAdapter.changeSelectedPosition(position)
                        catalogBottomGalleyRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }

            })
            if(SHOW_BOTTOM_GALLERY){
                image_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                catalogBottomGalleyRecyclerViewAdapter = CatalogBottomGalleyRecyclerViewAdapter(catalogImages!!, this, currentImage)
                image_recycler_view.adapter = catalogBottomGalleyRecyclerViewAdapter
            }
        }

        cross.setOnClickListener {
            activity?.finish()
        }
    }

    override fun onImageClick(adapterPosition: Int) {
        CatalogDetailPageAnalytics.trackEventClickIndexPicture(adapterPosition)
        view_pager_intermediary.setCurrentItem(adapterPosition, true)
        catalogBottomGalleyRecyclerViewAdapter.changeSelectedPosition(adapterPosition)
        catalogBottomGalleyRecyclerViewAdapter.notifyDataSetChanged()
    }
}