package com.tokopedia.reviewseller.feature.reviewreply.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.reviewdetail.view.model.FeedbackUiModel
import com.tokopedia.reviewseller.feature.reviewreply.di.component.ReviewReplyComponent
import com.tokopedia.reviewseller.feature.reviewreply.util.mapper.SellerReviewReplyMapper
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ProductReplyUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.model.ReplyTemplateUiModel
import com.tokopedia.reviewseller.feature.reviewreply.view.viewmodel.SellerReviewReplyViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.list.ListUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_seller_review_reply.*
import javax.inject.Inject

class SellerReviewReplyFragment: BaseDaggerFragment() {

    companion object {
        const val EXTRA_FEEDBACK_DATA = "EXTRA_FEEDBACK_DATA"
        const val EXTRA_PRODUCT_DATA = "EXTRA_PRODUCT_DATA"
        const val CACHE_OBJECT_ID = "CACHE_OBJECT_ID"
        const val EXTRA_SHOP_ID = "EXTRA_SHOP_ID"

    }

//    @Inject
//    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModelReviewReply: SellerReviewReplyViewModel? = null

    private var optionMenuReplyReview: ListUnify? = null
    private var bottomSheetReplyReview: BottomSheetUnify? = null

    private var feedbackUiModel: FeedbackUiModel? = null
    private var productReplyUiModel: ProductReplyUiModel? = null

    private var cacheManager: SaveInstanceCacheManager? = null

    private var shopId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        initData(savedInstanceState)
        super.onCreate(savedInstanceState)
        activity?.let {
            cacheManager = SaveInstanceCacheManager(it, savedInstanceState)
        }
        viewModelReviewReply = ViewModelProvider(this, viewModelFactory).get(SellerReviewReplyViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_review_reply, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar()
        initViewBottomSheet()
        observeLiveData()
    }

    override fun getScreenName(): String {
        return getString(R.string.title_review_reply)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_option_review_product_detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_option_product_detail -> {
                initBottomSheetReplyReview()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        viewModelReviewReply?.reviewTemplate?.removeObservers(this)
        viewModelReviewReply?.flush()
        super.onDestroy()
    }

    private fun observeLiveData() {
        hideData()

        viewModelReviewReply?.reviewTemplate?.observe(this, Observer {
            when(it) {
                is Success -> {
                    initWidgetView(it.data)
                }
                is Fail -> { }
            }
        })

        viewModelReviewReply?.getTemplateListReply(shopId)
    }

    private fun showData() {
        loaderReviewReply?.gone()
        productItemReplyWidget?.show()
        feedbackItemReplyWidget?.show()
        reviewReplyTextBoxWidget?.show()
    }

    private fun hideData() {
        loaderReviewReply?.show()
        productItemReplyWidget?.gone()
        feedbackItemReplyWidget?.gone()
        reviewReplyTextBoxWidget?.gone()
    }

    private fun initData(savedInstanceState: Bundle?) {
        context?.let {
            activity?.intent?.run {
                shopId = getStringExtra(EXTRA_SHOP_ID).toInt()
                val objectId = getStringExtra(CACHE_OBJECT_ID)
                val manager = if(savedInstanceState == null) {
                    SaveInstanceCacheManager(it, objectId)
                } else {
                    cacheManager
                }
                feedbackUiModel = manager?.get(EXTRA_FEEDBACK_DATA, FeedbackUiModel::class.java)
                productReplyUiModel = manager?.get(EXTRA_PRODUCT_DATA, ProductReplyUiModel::class.java)
            }
        }
    }

    private fun initWidgetView(data: List<ReplyTemplateUiModel>) {
        showData()
        productReplyUiModel?.let { productItemReplyWidget?.setItem(it) }
        feedbackUiModel?.let { feedbackItemReplyWidget?.setData(it) }
        reviewReplyTextBoxWidget?.setReplyAction(data)
    }

    private fun initToolbar() {
        activity?.run {
            (this as? AppCompatActivity)?.run {
                setSupportActionBar(review_reply_toolbar)
                supportActionBar?.title = getString(R.string.title_review_reply)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowTitleEnabled(true)
                setHasOptionsMenu(true)
            }
        }
    }

    override fun initInjector() {
        getComponent(ReviewReplyComponent::class.java).inject(this)
    }

    private fun initBottomSheetReplyReview() {
        val optionMenuReport = context?.let { SellerReviewReplyMapper.mapToItemUnifyMenuReport(it) }
        optionMenuReport?.let { optionMenuReplyReview?.setData(it) }
        val title = context?.getString(R.string.option_menu_label)
        bottomSheetReplyReview?.apply {
            setTitle(title.orEmpty())
            showCloseIcon = true
            setCloseClickListener {
                dismiss()
            }
        }

        optionMenuReplyReview?.let {
            it.onLoadFinish {
                it.setOnItemClickListener { _, _, position, _ ->
                    when (position) {
                        0 -> {
                            RouteManager.route(context, ApplinkConstInternalMarketplace.REVIEW_SELLER_REPORT)
                        }
                    }
                }
            }
        }

        fragmentManager?.let {
            bottomSheetReplyReview?.show(it, getString(R.string.option_menu_label))
        }
    }

    private fun initViewBottomSheet() {
        val viewMenu = View.inflate(context, R.layout.bottom_sheet_menu_option_review_reply, null)
        bottomSheetReplyReview = BottomSheetUnify()
        optionMenuReplyReview = viewMenu.findViewById(R.id.optionMenuReply)
        bottomSheetReplyReview?.setChild(viewMenu)
    }

}
