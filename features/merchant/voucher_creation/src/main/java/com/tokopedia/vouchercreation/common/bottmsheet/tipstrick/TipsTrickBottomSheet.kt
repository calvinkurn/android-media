package com.tokopedia.vouchercreation.common.bottmsheet.tipstrick

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.kotlin.extensions.view.*
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

class TipsTrickBottomSheet: BottomSheetUnify() {
    
    companion object {
        @JvmStatic
        fun createInstance(isPrivateVoucher: Boolean): TipsTrickBottomSheet {
            return TipsTrickBottomSheet().apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
                arguments = Bundle().apply {
                    putBoolean(IS_PRIVATE, isPrivateVoucher)
                }
            }
        }

        private const val IS_PRIVATE = "is_private"

        const val TAG = "TipsTrickBottomSheet"
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private val tipsTrickAdapter by lazy { TipsTrickAdapter() }
    private val carouselAdapter by lazy { TipsTrickCarouselAdapter(userSession.userId) }

    private val downloadImpressHolder = ImpressHolder()

    private var ctaDownloadCallback: () -> Unit = {}
    private var ctaShareCallback: () -> Unit = {}

    private val isPrivateVoucher: Boolean by lazy {
        arguments?.getBoolean(IS_PRIVATE) ?: false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        initInjector()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
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

    private fun initBottomSheet() {
        context?.run {
            val child = LayoutInflater.from(this)
                    .inflate(R.layout.bottomsheet_mvc_tips_trick, ConstraintLayout(this), false)

            setTitle(getBottomSheetTitle())
            setChild(child)
        }
    }

    private fun getBottomSheetTitle(): String {
        return if (isPrivateVoucher) {
            context?.getString(R.string.mvc_tips_and_trick_private).toBlankOrString()
        } else {
            context?.getString(R.string.mvc_tips_and_trick_public).toBlankOrString()
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
            context?.getString(R.string.mvc_private_voucher_tips_trick_info).toBlankOrString()
        } else {
            context?.getString(R.string.mvc_public_voucher_tips_trick_info).toBlankOrString()
        }
    }

    private fun setupCarouselImage(child: View) = with(child) {
        rvMvcImageCarousel.run {
            adapter = carouselAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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
                        context?.getString(R.string.mvc_create_public_voucher_display_product_page).toBlankOrString(),
                        VoucherDisplay.PUBLIC_PRODUCT
                ),
                TipsTrickCarouselModel(
                        context?.getString(R.string.mvc_create_public_voucher_display_shop_page).toBlankOrString(),
                        VoucherDisplay.PUBLIC_SHOP
                ),
                TipsTrickCarouselModel(
                        context?.getString(R.string.mvc_create_public_voucher_display_cart_page).toBlankOrString(),
                        VoucherDisplay.PUBLIC_CART
                )
        )
        carouselAdapter.setItems(items)
    }

    private fun showPrivateVoucherCarousel() {
        val items = listOf(
                TipsTrickCarouselModel(
                        context?.getString(R.string.mvc_create_private_voucher_display_download_voucher).toBlankOrString(),
                        VoucherDisplay.PRIVATE_DOWNLOAD
                ),
                TipsTrickCarouselModel(
                        context?.getString(R.string.mvc_create_private_voucher_display_socmed_post).toBlankOrString(),
                        VoucherDisplay.PRIVATE_SOCMED
                ),
                TipsTrickCarouselModel(
                        context?.getString(R.string.mvc_create_private_voucher_display_chat_share).toBlankOrString(),
                        VoucherDisplay.PRIVATE_CHAT
                )
        )
        carouselAdapter.setItems(items)
    }

    private fun setupTipsAndTrick(child: View) = with(child.rvMvcTipsTrick) {
        adapter = tipsTrickAdapter
        layoutManager = object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }

        showTipsAndTrick()
    }

    private fun showTipsAndTrick() {
        val items = listOf(
                TipsTrickModel(
                        context?.getString(R.string.mvc_show_voucher_to_your_social_media_title).toBlankOrString(),
                        context?.getString(R.string.mvc_show_voucher_to_your_social_media_description).toBlankOrString()
                ),
                TipsTrickModel(
                        context?.getString(R.string.mvc_giveaway_for_followers_title).toBlankOrString(),
                        context?.getString(R.string.mvc_giveaway_for_followers_description).toBlankOrString()
                ),
                TipsTrickModel(
                        context?.getString(R.string.mvc_post_voucher_on_shop_feed_title).toBlankOrString(),
                        context?.getString(R.string.mvc_post_voucher_on_shop_feed_description).toBlankOrString()
                ),
                TipsTrickModel(
                        context?.getString(R.string.mvc_set_as_shop_cover_title).toBlankOrString(),
                        context?.getString(R.string.mvc_set_as_shop_cover_description).toBlankOrString()
                ),
                TipsTrickModel(
                        context?.getString(R.string.mvc_share_on_messenger_group_title).toBlankOrString(),
                        context?.getString(R.string.mvc_share_on_messenger_group_desription).toBlankOrString()
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
        show(fm, TAG)
    }
}