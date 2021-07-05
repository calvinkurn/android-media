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
import com.tokopedia.catalog.model.raw.CatalogImage
import kotlinx.android.synthetic.main.fragment_catalog_gallery.*

class CatalogGalleryFragment : Fragment() {
    private var catalogImages: ArrayList<CatalogImage>? = null
    private var currentImage: Int = -1
    private var catalogId : String = ""
    private lateinit var catalogBottomGalleyRecyclerViewAdapter: CatalogBottomGalleyRecyclerViewAdapter

    companion object {
        private const val ARG_CATALOG_ID = "ARG_CATALOG_ID"
        private const val ARG_EXTRA_IMAGES = "ARG_EXTRA_IMAGES"
        private const val ARG_EXTRA_CURRENT_IMAGE = "ARG_EXTRA_CURRENT_IMAGE"
        private const val LEFT = "left"
        private const val RIGHT = "right"

        private const val SHOW_BOTTOM_GALLERY = false

        fun newInstance(catalogId : String,currentItem: Int, catalogImages: ArrayList<CatalogImage>): CatalogGalleryFragment {
            val fragment = CatalogGalleryFragment()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_EXTRA_IMAGES, catalogImages)
            bundle.putString(ARG_CATALOG_ID, catalogId)
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
            catalogImages = requireArguments().getParcelableArrayList(ARG_EXTRA_IMAGES)
            currentImage = requireArguments().getInt(ARG_EXTRA_CURRENT_IMAGE)
            catalogId = requireArguments().getString(ARG_CATALOG_ID) ?: ""
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
                    previousPosition = position
                    if(SHOW_BOTTOM_GALLERY){
                        catalogBottomGalleyRecyclerViewAdapter.changeSelectedPosition(position)
                        catalogBottomGalleyRecyclerViewAdapter.notifyDataSetChanged()
                    }
                }

            })
            if(SHOW_BOTTOM_GALLERY){
                image_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                catalogBottomGalleyRecyclerViewAdapter = CatalogBottomGalleyRecyclerViewAdapter(catalogImages!!, onImageClick, currentImage)
                image_recycler_view.adapter = catalogBottomGalleyRecyclerViewAdapter
            }
        }

        cross.setOnClickListener {
            activity?.finish()
        }
    }

    private val onImageClick : (Int) -> Unit = {adapterPosition ->
        view_pager_intermediary.setCurrentItem(adapterPosition, true)
        catalogBottomGalleyRecyclerViewAdapter.changeSelectedPosition(adapterPosition)
        catalogBottomGalleyRecyclerViewAdapter.notifyDataSetChanged()
    }
}