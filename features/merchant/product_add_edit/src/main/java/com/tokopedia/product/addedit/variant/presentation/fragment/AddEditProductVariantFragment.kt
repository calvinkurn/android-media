package com.tokopedia.product.addedit.variant.presentation.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexboxLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.common.constant.AddEditProductConstants
import com.tokopedia.product.addedit.common.util.HorizontalItemDecoration
import com.tokopedia.product.addedit.variant.di.AddEditProductVariantComponent
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantPhotoAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantTypeAdapter
import com.tokopedia.product.addedit.variant.presentation.adapter.VariantValueAdapter
import com.tokopedia.product.addedit.variant.presentation.viewholder.VariantTypeViewHolder
import kotlinx.android.synthetic.main.add_edit_product_variant_photo_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_type_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level1_layout.*
import kotlinx.android.synthetic.main.add_edit_product_variant_value_level2_layout.*

class AddEditProductVariantFragment : BaseDaggerFragment(), VariantTypeViewHolder.OnVariantTypeClickListener {

    companion object {
        fun createInstance(cacheManagerId: String?): Fragment {
            return AddEditProductVariantFragment().apply {
                arguments = Bundle().apply {
                    putString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID, cacheManagerId)
                }
            }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(AddEditProductVariantComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cacheManagerId = arguments?.getString(AddEditProductConstants.EXTRA_CACHE_MANAGER_ID)
        val saveInstanceCacheManager = SaveInstanceCacheManager(requireContext(), cacheManagerId)

        cacheManagerId?.run {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_product_variant, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val variantTypeAdapter = VariantTypeAdapter(this)
        val variantValueAdapter = VariantValueAdapter()
        val variantPhotoAdapterAdapter = VariantPhotoAdapter()
        recyclerViewVariantType.adapter = variantTypeAdapter
        recyclerViewVariantValueLevel1.adapter = variantValueAdapter
        recyclerViewVariantValueLevel2.adapter = variantValueAdapter
        recyclerViewVariantPhoto.adapter = variantPhotoAdapterAdapter
        setRecyclerViewToFlex(recyclerViewVariantType)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel1)
        setRecyclerViewToFlex(recyclerViewVariantValueLevel2)
        setRecyclerViewToHorizontal(recyclerViewVariantPhoto)

        Handler().postDelayed({
            val variants: List<String> = listOf("ukuran yang menentukan semua yang terukur", "warna", "rasya", "dsdsdsf", "asdasdas dasd", "sadsdsdsdasda")
            variantTypeAdapter.setData(variants)
            variantValueAdapter.setData(variants)
            variantPhotoAdapterAdapter.setData(variants)
        }, 1000)
    }

    override fun onVariantTypeClicked(position: Int) {

    }

    private fun setRecyclerViewToFlex(recyclerView: RecyclerView) {
        val flexboxLayoutManager = FlexboxLayoutManager(context)
        flexboxLayoutManager.alignItems = AlignItems.FLEX_START
        recyclerView.apply {
            layoutManager = flexboxLayoutManager
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }

    private fun setRecyclerViewToHorizontal(recyclerView: RecyclerView) {
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizontalItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_lvl3)))
        }
    }
    fun onBackPressed() {
        activity?.finish()
    }

}
