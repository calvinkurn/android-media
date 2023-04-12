package com.tokopedia.feedplus.presentation.customview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.feedcomponent.bottomsheets.ProductItemInfoBottomSheet
import com.tokopedia.feedcomponent.databinding.ItemPosttagBinding
import com.tokopedia.feedcomponent.view.adapter.bottomsheetadapter.ProductInfoBottomSheetAdapter
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.FeedTaggedProductBottomSheetCardView
import com.tokopedia.feedcomponent.view.adapter.viewholder.posttag.FeedTaggedProductViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.posttag.ProductPostTagModelNew
import com.tokopedia.feedplus.R
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * Created By : Muhammad Furqan on 12/04/23
 */
class FeedProductListBottomSheet : BottomSheetUnify(), FeedTaggedProductViewHolder.Listener {

    private var _binding: ItemPosttagBinding? = null
    private val binding: ItemPosttagBinding
        get() = _binding!!

    private var listener: ProductItemInfoBottomSheet.Listener? = null

    private val adapter by lazy {
        listener?.let {
            ProductInfoBottomSheetAdapter(it, this)
        }
    }

    private var postId: String = "0"
    private var positionInFeed: Int = 0
    private var shopId: String = "0"
    private var shopName: String = ""
    private var playChannelId: String = "0"
    private var postType: String = ""
    private var saleType: String = ""
    private var saleStatus: String = ""
    private var hasVoucher: Boolean = false
    private var authorType: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ItemPosttagBinding.inflate(inflater, container, false)
        setTitle(getString(R.string.feed_product_list_bottom_sheet_title))
        setChild(binding.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.rvPosttag.apply {
            show()
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            layoutManager = rvLayoutManager
        }
    }

    override fun onProductClicked(
        viewHolder: FeedTaggedProductBottomSheetCardView,
        product: ProductPostTagModelNew
    ) {
//        TODO("Not yet implemented")
    }

    override fun onButtonMoveToCartProduct(
        viewHolder: FeedTaggedProductBottomSheetCardView,
        product: ProductPostTagModelNew
    ) {
//        TODO("Not yet implemented")
    }

    override fun onButtonAddToCartProduct(
        viewHolder: FeedTaggedProductBottomSheetCardView,
        product: ProductPostTagModelNew
    ) {
//        TODO("Not yet implemented")
    }

}
