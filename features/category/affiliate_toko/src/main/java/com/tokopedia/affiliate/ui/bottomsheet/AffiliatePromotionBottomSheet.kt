package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.affiliate.AffiliateAnalytics
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromotionBottomSheet : BottomSheetUnify(), ShareButtonInterface {

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var contentView: View? = null
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(this))
    private var productId : String = ""
    private var currentName: String? = null
    private var currentServiceFormat = ""
    private var originScreen = ORIGIN_PROMOSIKAN
    private var url: String? = null
    private var identifier: String? = null
    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf(
            AffiliateShareModel("Instagram", IconUnify.INSTAGRAM,"instagram",3),
            AffiliateShareModel("Tiktok", IconUnify.TIKTOK,"tiktok",9),
            AffiliateShareModel("YouTube", IconUnify.YOUTUBE,"youtube",13),
            AffiliateShareModel("Facebook", IconUnify.FACEBOOK,"facebook",1),
            AffiliateShareModel("Twitter", IconUnify.TWITTER,"twitter",10),
            AffiliateShareModel("Website/Blog", IconUnify.GLOBE,"website",11),
            AffiliateShareModel("WhatsApp", IconUnify.WHATSAPP,"whatsapp",12),
            AffiliateShareModel("Line", IconUnify.LINE,"line",4),
            AffiliateShareModel("Lainnya",null,"others", 0)
    )

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliatePromotionBSViewModel: AffiliatePromotionBSViewModel

    companion object {
        private const val COPY_LABEL = "Tokopedia"
        private const val KEY_PRODUCT_ID = "KEY_PRODUCT_ID"
        private const val KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME"
        private const val KEY_PRODUCT_IMAGE = "KEY_PRODUCT_IMAGE"
        private const val KEY_PRODUCT_URL = "KEY_PRODUCT_URL"
        private const val KEY_PRODUCT_IDENTIFIER = "KEY_PRODUCT_IDENTIFIER"
        private const val KEY_ORIGIN = "KEY_ORIGIN"

        const val ORIGIN_PROMOSIKAN = 1
        const val ORIGIN_HOME = 2

        fun newInstance(productId : String, productName: String, productImage: String,
                        productUrl: String, productIdentifier: String, origin : Int = ORIGIN_PROMOSIKAN): AffiliatePromotionBottomSheet {
            return AffiliatePromotionBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(KEY_PRODUCT_ID,productId)
                    putString(KEY_PRODUCT_NAME, productName)
                    putString(KEY_PRODUCT_IMAGE, productImage)
                    putString(KEY_PRODUCT_URL, productUrl)
                    putString(KEY_PRODUCT_IDENTIFIER, productIdentifier)
                    putInt(KEY_ORIGIN,origin)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        affiliatePromotionBSViewModel = ViewModelProviders.of(this, viewModelProvider).get(AffiliatePromotionBSViewModel::class.java)
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun init() {
        showCloseIcon = true
        showKnob = false
        setTitle(getString(R.string.affiliate_where_to_promote))
        contentView = View.inflate(context,
                R.layout.affiliate_promotion_bottom_sheet, null)

        contentView?.findViewById<RecyclerView>(R.id.share_rv)?.let {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
        contentView?.run {
            arguments?.let { bundle ->
                findViewById<Typography>(R.id.product_name).text = bundle.getString(KEY_PRODUCT_NAME)
                ImageHandler.loadImageCover2(findViewById(R.id.product_image), bundle.getString(KEY_PRODUCT_IMAGE))
                productId = bundle.getString(KEY_PRODUCT_ID,"")
                url = bundle.getString(KEY_PRODUCT_URL, "")
                identifier = bundle.getString(KEY_PRODUCT_IDENTIFIER)
                originScreen = bundle.getInt(KEY_ORIGIN, ORIGIN_PROMOSIKAN)
            }
            setObservers(this)
        }
        setChild(contentView)
        sendScreenEvent()
    }

    private fun initInject() {
        getComponent().injectPromotionBottomSheet(this)
    }

    private fun getComponent(): AffiliateComponent =
            DaggerAffiliateComponent
                    .builder()
                    .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                    .build()

    private fun setObservers(contentView: View) {
        affiliatePromotionBSViewModel.generateLinkData().observe(this, {
            var eventCategory = AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP_B_S
            if(originScreen == ORIGIN_HOME){
                eventCategory = AffiliateAnalytics.CategoryKeys.HOME_PORTAL_B_S
            }
            it?.let { data ->
                AffiliateAnalytics.sendEvent(
                        AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                        AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK,
                        eventCategory,
                        "$productId-${data.linkID}-$currentServiceFormat",userSessionInterface.userId)
                val clipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                clipboardManager.setPrimaryClip(ClipData.newPlainText(COPY_LABEL, data.url?.shortURL))
                Toaster.build(contentView.rootView, getString(R.string.affiliate_link_generated_succesfully, currentName),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            } ?: kotlin.run {
                if(originScreen == ORIGIN_HOME){
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            AffiliateAnalytics.ActionKeys.IMPRESSION_LINK_GEN_ERROR,
                            eventCategory,
                            "$productId-$currentServiceFormat",userSessionInterface.userId)
                }else {
                    AffiliateAnalytics.sendEvent(
                            AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                            AffiliateAnalytics.ActionKeys.IMPRESSION_LINK_GEN_ERROR,
                            eventCategory,
                            "$productId-$currentServiceFormat",userSessionInterface.userId)
                }
            }
        })

        affiliatePromotionBSViewModel.loading().observe(this, { isLoad ->
            if (isLoad != null) {
                loading(isLoad)
            }
        })

        affiliatePromotionBSViewModel.getErrorMessage().observe(this, { error ->
            if (error != null) {
                Toaster.build(contentView.rootView, error,
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        })
    }

    private fun loading(stop: Boolean) {
        for (visitable in listVisitable) {
            val updateVisitable = visitable as AffiliateShareModel
            if (currentName == updateVisitable.name) {
                updateVisitable.buttonLoad = stop
                adapter.setElement(adapter.list.indexOf(visitable), updateVisitable)
                break
            }
        }
    }

    private fun sendScreenEvent(){
        if(originScreen == ORIGIN_HOME){
            AffiliateAnalytics.sendEvent(
                    AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                    AffiliateAnalytics.ActionKeys.IMPRESSION_HOME_PORTAL_B_S,
                    AffiliateAnalytics.CategoryKeys.HOME_PORTAL_B_S,
                    "",userSessionInterface.userId)
        }else {
            AffiliateAnalytics.sendEvent(
                    AffiliateAnalytics.EventKeys.EVENT_VALUE_VIEW,
                    AffiliateAnalytics.ActionKeys.IMPRESSION_PROMOSIKAN_SRP_B_S,
                    AffiliateAnalytics.CategoryKeys.PROMOSIKAN_SRP_B_S,
                    "",userSessionInterface.userId)
        }
    }

    override fun onShareButtonClick(name : String?, id: Int?, serviceFormat :String?) {
        currentName = name
        currentServiceFormat = serviceFormat ?: ""
        affiliatePromotionBSViewModel.affiliateGenerateLink(id, url, identifier)
    }

}
