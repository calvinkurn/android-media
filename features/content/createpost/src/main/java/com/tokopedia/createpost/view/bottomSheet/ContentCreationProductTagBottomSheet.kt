package com.tokopedia.createpost.view.bottomSheet

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.databinding.ContentCreationProductTagBottomSheetBinding
import com.tokopedia.createpost.view.adapter.CreatePostTagAdapter
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import com.tokopedia.content.common.R as contentcommonR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class ContentCreationProductTagBottomSheet : BottomSheetUnify() {

    private var listener: CreateContentPostCommonListener? = null
    private var productData: List<RelatedProductItem>? = null
    private var mediaType: String = ""
    private val createPostTagAdapter: CreatePostTagAdapter by lazy {
        CreatePostTagAdapter(onDeleteProduct = this::onDeleteProduct)
    }

    private var _binding: ContentCreationProductTagBottomSheetBinding? = null
    private val binding: ContentCreationProductTagBottomSheetBinding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBottomSheet() {
        _binding = ContentCreationProductTagBottomSheetBinding.inflate(layoutInflater)
        setChild(binding.root)
        setTitle(getString(contentcommonR.string.feed_content_product_bottom_sheet_name))
    }

    private fun initAdapter() {
        productData?.let {
            createPostTagAdapter.updateProduct(it)
            binding.rvProductTag.adapter = createPostTagAdapter
            binding.rvProductTag.layoutManager = LinearLayoutManager(context, VERTICAL, false)
        }
    }

    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    private fun onDeleteProduct(position: Int) {
        view?.rootView?.let {
            Toaster.toasterCustomBottomHeight = requireContext().resources.getDimensionPixelSize(unifyprinciplesR.dimen.layout_lvl6)
            Toaster.build(it,
                getString(contentcommonR.string.feed_content_delete_toaster_text),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_NORMAL).show()
        }

        if (productData?.size == 1)
            dismiss()
        listener?.deleteItemFromProductTagList(position,
            productData?.get(position)?.id.toString(),
            false,
            mediaType)
    }

    fun show(
        bundle: Bundle,
        childFragmentManager: FragmentManager,
        productList: List<RelatedProductItem>,
        listener: CreateContentPostCommonListener,
        mediaType: String,
    ) {
        arguments = bundle
        val tag = "ContentCreationProductTagBottomSheet"
        this.productData = productList
        this.listener = listener
        this.mediaType = mediaType
        show(childFragmentManager, tag)

    }

}

