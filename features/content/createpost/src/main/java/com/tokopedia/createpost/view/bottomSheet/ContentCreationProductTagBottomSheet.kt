package com.tokopedia.createpost.view.bottomSheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.createpost.common.view.viewmodel.RelatedProductItem
import com.tokopedia.createpost.createpost.R
import com.tokopedia.createpost.view.adapter.CreatePostTagAdapter
import com.tokopedia.createpost.view.listener.CreateContentPostCommonListener
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.content_creation_product_tag_bottom_sheet.*

class ContentCreationProductTagBottomSheet : BottomSheetUnify() {

    private val childLayoutRes = R.layout.content_creation_product_tag_bottom_sheet
    private var productData: List<RelatedProductItem>? = null
    private var mediaType: String = ""
    private val createPostTagAdapter: CreatePostTagAdapter by lazy {
        CreatePostTagAdapter(onDeleteProduct = this::onDeleteProduct)
    }
    private var listener: CreateContentPostCommonListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
    }

    private fun initBottomSheet() {
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
            null, false)
        setChild(childView)
        setTitle(getString(com.tokopedia.content.common.R.string.feed_content_product_bottom_sheet_name))
    }


    private fun setDefaultParams() {
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }


    private fun initAdapter() {
        productData?.let {
            createPostTagAdapter.updateProduct(it)
            rv_product_tag.adapter = createPostTagAdapter
            rv_product_tag.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }
    }

    private fun onDeleteProduct(position: Int) {
        view?.rootView?.let {
            Toaster.toasterCustomBottomHeight = requireContext().resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl6)
            Toaster.build(it,
                getString(com.tokopedia.content.common.R.string.feed_content_delete_toaster_text),
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

