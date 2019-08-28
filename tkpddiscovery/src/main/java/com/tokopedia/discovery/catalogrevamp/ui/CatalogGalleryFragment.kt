package com.tokopedia.discovery.catalogrevamp.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.discovery.R
import com.tokopedia.discovery.catalogrevamp.adapter.CatalogGalleyRecyclerViewAdapter
import com.tokopedia.discovery.catalogrevamp.adapter.CatalogImageAdapter
import com.tokopedia.discovery.catalogrevamp.model.ProductCatalogResponse.ProductCatalogQuery.Data.Catalog.CatalogImage
import kotlinx.android.synthetic.main.fragment_catalog_gallery.*

class CatalogGalleryFragment : Fragment(), CatalogGalleyRecyclerViewAdapter.Listener {
    private var catalogImages: ArrayList<CatalogImage>? = null
    private var currentImage: Int = -1
    private var listener: Listener? = null
    private lateinit var catalogGalleyRecyclerViewAdapter:CatalogGalleyRecyclerViewAdapter

    companion object {
        private const val ARG_EXTRA_IMAGES = "ARG_EXTRA_IMAGES"
        private const val ARG_EXTRA_CURRENT_IMAGE = "ARG_EXTRA_CURRENT_IMAGE"

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
            val catalogImageAdapter = CatalogImageAdapter(catalogImages!!, null)
            view_pager_intermediary.adapter = catalogImageAdapter
            view_pager_intermediary.setCurrentItem(currentImage, true)
            view_pager_intermediary.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
                override fun onPageScrollStateChanged(p0: Int) {}

                override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(position: Int) {
                    catalogGalleyRecyclerViewAdapter.changeSelectedPosition(position)
                    catalogGalleyRecyclerViewAdapter.notifyDataSetChanged()
                }

            })
            image_recycler_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            catalogGalleyRecyclerViewAdapter = CatalogGalleyRecyclerViewAdapter(catalogImages!!, this, currentImage)
            image_recycler_view.adapter = catalogGalleyRecyclerViewAdapter
        }

        cross.setOnClickListener {
            listener?.onCrossClick()
        }
    }

    override fun onImageClick(adapterPosition: Int) {
        view_pager_intermediary.setCurrentItem(adapterPosition, true)
        catalogGalleyRecyclerViewAdapter.changeSelectedPosition(adapterPosition)
        catalogGalleyRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun setListener(listener: Listener){
        this.listener = listener
    }

    interface Listener {
        fun onCrossClick()
    }

}