package com.tokopedia.vouchercreation.common.bottmsheet.tipstrick

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationAnalyticConstant
import com.tokopedia.vouchercreation.common.analytics.VoucherCreationTracking
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.adapter.TipsTrickAdapter
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.adapter.TipsTrickCarouselAdapter
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.model.TipsTrickCarouselModel
import com.tokopedia.vouchercreation.common.bottmsheet.tipstrick.model.TipsTrickModel
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.di.component.DaggerVoucherCreationComponent
import com.tokopedia.vouchercreation.common.utils.decorator.VoucherDisplayItemDecoration
import com.tokopedia.vouchercreation.create.view.enums.VoucherDisplay
import kotlinx.android.synthetic.main.bottomsheet_mvc_tips_trick.*
import kotlinx.android.synthetic.main.bottomsheet_mvc_tips_trick.view.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 08/05/20
 */

class TipsTrickBottomSheet(
        private val mContext: Context,
        private val isPrivateVoucher: Boolean
) : BottomSheetUnify() {

    @Inject
    lateinit var userSession: UserSessionInterface

    private val tipsTrickAdapter by lazy { TipsTrickAdapter() }
    private val carouselAdapter by lazy { TipsTrickCarouselAdapter(userSession.userId) }

    private val downloadImpressHolder = ImpressHolder()

    private var ctaDownloadCallback: () -> Unit = {}
    private var ctaShareCallback: () -> Unit = {}

    init {
        val child = LayoutInflater.from(mContext)
                .inflate(R.layout.bottomsheet_mvc_tips_trick, ConstraintLayout(mContext), false)

        setTitle(getBottomSheetTitle())
        setChild(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        view.setupBottomSheetChildNoMargin()
    }

    private fun initInjector() {
        DaggerVoucherCreationComponent.builder()
                .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
                .build()
                .inject(this)
    }

    private fun getBottomSheetTitle(): String {
        return if (isPrivateVoucher) {
            mContext.getString(R.string.mvc_tips_and_trick_private)
        } else {
            mContext.getString(R.string.mvc_tips_and_trick_public)
        }
    }

    private fun getVoucherTypeIcon(): Int {
        return if (isPrivateVoucher) {
            R.drawable.ic_mvc_khusus
        } else {
            R.drawable.ic_mvc_publik
        }
    }

    private fun getCarouselInfo(): String {
        return if (isPrivateVoucher) {
            mContext.getString(R.string.mvc_private_voucher_tips_trick_info)
        } else {
            mContext.getString(R.string.mvc_public_voucher_tips_trick_info)
        }
    }

    private fun setupCarouselImage(child: View) = with(child) {
        rvMvcImageCarousel.run {
            adapter = carouselAdapter
            layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val mLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                    val currentPosition = mLayoutManager?.findFirstCompletelyVisibleItemPosition().orZero()
                    this@with.pageControlMvcCarousel.setCurrentIndicator(currentPosition)
                }
            })
            try {
                PagerSnapHelper().attachToRecyclerView(this)
            } catch (e: IllegalArgumentException) {
                Timber.e(e)
            }
            addItemDecoration(VoucherDisplayItemDecoration(context))
        }

        if (isPrivateVoucher) {
            showPrivateVoucherCarousel()
        } else {
            showPublicVoucherCarousel()
        }
        pageControlMvcCarousel.setIndicator(carouselAdapter.itemCount)
    }

    private fun showPublicVoucherCarousel() {
        val items = listOf(
                TipsTrickCarouselModel(
                        mContext.getString(R.string.mvc_create_public_voucher_display_product_page),
                        VoucherDisplay.PUBLIC_PRODUCT
                ),
                TipsTrickCarouselModel(
                        mContext.getString(R.string.mvc_create_public_voucher_display_shop_page),
                        VoucherDisplay.PUBLIC_SHOP
                ),
                TipsTrickCarouselModel(
                        mContext.getString(R.string.mvc_create_public_voucher_display_cart_page),
                        VoucherDisplay.PUBLIC_CART
                )
        )
        carouselAdapter.setItems(items)
    }

    private fun showPrivateVoucherCarousel() {
        val items = listOf(
                TipsTrickCarouselModel(
                        mContext.getString(R.string.mvc_create_private_voucher_display_download_voucher),
                        VoucherDisplay.PRIVATE_DOWNLOAD
                ),
                TipsTrickCarouselModel(
                        mContext.getString(R.string.mvc_create_private_voucher_display_socmed_post),
                        VoucherDisplay.PRIVATE_SOCMED
                ),
                TipsTrickCarouselModel(
                        mContext.getString(R.string.mvc_create_private_voucher_display_chat_share),
                        VoucherDisplay.PRIVATE_CHAT
                )
        )
        carouselAdapter.setItems(items)
    }

    private fun setupTipsAndTrick(child: View) = with(child.rvMvcTipsTrick) {
        adapter = tipsTrickAdapter
        layoutManager = object : LinearLayoutManager(mContext) {
            override fun canScrollVertically(): Boolean = false
        }

        showTipsAndTrick()
    }

    private fun showTipsAndTrick() {
        val items = listOf(
                TipsTrickModel(
                        mContext.getString(R.string.mvc_show_voucher_to_your_social_media_title),
                        mContext.getString(R.string.mvc_show_voucher_to_your_social_media_description)
                ),
                TipsTrickModel(
                        mContext.getString(R.string.mvc_giveaway_for_followers_title),
                        mContext.getString(R.string.mvc_giveaway_for_followers_description)
                ),
                TipsTrickModel(
                        mContext.getString(R.string.mvc_post_voucher_on_shop_feed_title),
                        mContext.getString(R.string.mvc_post_voucher_on_shop_feed_description)
                ),
                TipsTrickModel(
                        mContext.getString(R.string.mvc_set_as_shop_cover_title),
                        mContext.getString(R.string.mvc_set_as_shop_cover_description)
                ),
                TipsTrickModel(
                        mContext.getString(R.string.mvc_share_on_messenger_group_title),
                        mContext.getString(R.string.mvc_share_on_messenger_group_desription)
                )
        )
        tipsTrickAdapter.setItems(items)
    }

    private fun setupView() {
        view?.run {
            tvMvcCarouselInfo.text = getCarouselInfo().parseAsHtml()
            icMvcCarouselInfo.loadImageDrawable(getVoucherTypeIcon())

            btnMvcTipsTrickDownload.setOnClickListener {
                ctaDownloadCallback()
            }
            btnMvcTipsTrickShare.setOnClickListener {
                ctaShareCallback()
            }
            setupTipsAndTrick(this)
            setupCarouselImage(this)
        }
        btnMvcTipsTrickDownload?.addOnImpressionListener(downloadImpressHolder) {
            VoucherCreationTracking.sendVoucherDetailClickTracking(
                    status = VoucherStatusConst.ONGOING,
                    action = VoucherCreationAnalyticConstant.EventAction.Impression.DOWNLOAD_VOUCHER,
                    userId = userSession.userId
            )
        }
    }

    private fun View.setupBottomSheetChildNoMargin() {
        val initialPaddingTop = paddingTop
        val initialPaddingBottom = paddingBottom
        val initialPaddingLeft = paddingLeft
        val initialPaddingRight = paddingRight
        setPadding(0,initialPaddingTop,0,initialPaddingBottom)
        bottomSheetHeader.setPadding(initialPaddingLeft, 0, initialPaddingRight, 0)
    }

    fun setOnDownloadClickListener(action: () -> Unit): TipsTrickBottomSheet {
        this.ctaDownloadCallback = action
        return this
    }

    fun setOnShareClickListener(action: () -> Unit): TipsTrickBottomSheet {
        this.ctaShareCallback = action
        return this
    }

    fun show(fm: FragmentManager) {
        show(fm, this::class.java.simpleName)
    }
}