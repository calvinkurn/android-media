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
import com.tokopedia.affiliate.interfaces.AddSocialInterface
import com.tokopedia.affiliate.interfaces.ShareButtonInterface
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.affiliate.viewmodel.AffiliatePromotionBSViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePromotionBottomSheet : BottomSheetUnify(), ShareButtonInterface , AddSocialInterface{

    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private var contentView: View? = null
    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(shareButtonInterface = this, addSocialInterface = this))
    private var productId : String = ""
    private var currentName: String? = null
    private var currentServiceFormat = ""
    private var originScreen = ORIGIN_PROMOSIKAN
    private var url: String? = null
    private var identifier: String? = null
    private var isLinkGenerationEnabled = true

    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private var sheetType = SheetType.LINK_GENERATION
    private var affiliatePromotionBottomSheetInterface : AffiliatePromotionBottomSheetInterface? = null
    private var selectedIds = arrayListOf<Int>()

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory
    private lateinit var affiliatePromotionBSViewModel: AffiliatePromotionBSViewModel

    companion object {

        enum class SheetType(type : Int) {
            LINK_GENERATION(1),
            ADD_SOCIAL(2)
        }

        private const val COPY_LABEL = "Tokopedia"
        private const val KEY_PRODUCT_ID = "KEY_PRODUCT_ID"
        private const val KEY_PRODUCT_NAME = "KEY_PRODUCT_NAME"
        private const val KEY_PRODUCT_IMAGE = "KEY_PRODUCT_IMAGE"
        private const val KEY_PRODUCT_URL = "KEY_PRODUCT_URL"
        private const val KEY_PRODUCT_IDENTIFIER = "KEY_PRODUCT_IDENTIFIER"
        private const val KEY_ORIGIN = "KEY_ORIGIN"
        private const val KEY_LINK_GEN_ENABLED = "KEY_LINK_GEN_ENABLED"
        private const val PERNAH_DIBELI = "pernah dibeli"
        private const val PERNAH_DILIHAT = "pernah dilihat"

        const val ORIGIN_PROMOSIKAN = 1
        const val ORIGIN_HOME = 2
        const val ORIGIN_PORTFOLIO = 3
        const val ORIGIN_PERNAH_DIBELI_PROMOSIKA = 4
        const val ORIGIN_TERAKHIR_DILIHAT = 5

        fun newInstance(bottomSheetType : SheetType, bottomSheetInterface : AffiliatePromotionBottomSheetInterface?,
                        idArray : ArrayList<Int>?,
                        productId : String, productName: String, productImage: String,
                        productUrl: String, productIdentifier: String, origin : Int = ORIGIN_PROMOSIKAN,
                        isLinkGenerationEnabled :Boolean = true): AffiliatePromotionBottomSheet {
            return AffiliatePromotionBottomSheet().apply {
                sheetType = bottomSheetType
                affiliatePromotionBottomSheetInterface = bottomSheetInterface
                selectedIds = idArray ?: arrayListOf()
                arguments = Bundle().apply {
                    putString(KEY_PRODUCT_ID,productId)
                    putString(KEY_PRODUCT_NAME, productName)
                    putString(KEY_PRODUCT_IMAGE, productImage)
                    putString(KEY_PRODUCT_URL, productUrl)
                    putString(KEY_PRODUCT_IDENTIFIER, productIdentifier)
                    putInt(KEY_ORIGIN,origin)
                    putBoolean(KEY_LINK_GEN_ENABLED,isLinkGenerationEnabled)
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
        if (sheetType == SheetType.ADD_SOCIAL)
            setTitle(getString(R.string.affiliate_add_social_media))
        else
            setTitle(getString(R.string.affiliate_where_to_promote))
        contentView = View.inflate(context,
                R.layout.affiliate_promotion_bottom_sheet, null)

        afterViewSet()
        setChild(contentView)
        sendScreenEvent()
    }

    private fun afterViewSet() {
        contentView?.run {
            arguments?.let { bundle ->
                findViewById<Typography>(R.id.product_name).text = bundle.getString(KEY_PRODUCT_NAME)
                ImageHandler.loadImageCover2(findViewById(R.id.product_image), bundle.getString(KEY_PRODUCT_IMAGE))
                productId = bundle.getString(KEY_PRODUCT_ID,"")
                url = bundle.getString(KEY_PRODUCT_URL, "")
                identifier = bundle.getString(KEY_PRODUCT_IDENTIFIER)
                originScreen = bundle.getInt(KEY_ORIGIN, ORIGIN_PROMOSIKAN)
                isLinkGenerationEnabled = bundle.getBoolean(KEY_LINK_GEN_ENABLED)
            }

            if(sheetType == SheetType.ADD_SOCIAL){
                findViewById<IconUnify>(R.id.product_image).hide()
                findViewById<Typography>(R.id.product_name).hide()
            }

            setObservers(this)
        }
        contentView?.findViewById<RecyclerView>(R.id.share_rv)?.let {
            addDataInRecyclerView()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    private fun addDataInRecyclerView() {
        listVisitable = arrayListOf<Visitable<AffiliateAdapterTypeFactory>>(
            AffiliateShareModel("Instagram", IconUnify.INSTAGRAM,"instagram",3,sheetType,
                    "Contoh: instagram.com/tokopedia",false,isChecked = false, isLinkGenerationEnabled),
            AffiliateShareModel("Tiktok", IconUnify.TIKTOK,"tiktok",9,sheetType,
                    "Contoh: tiktok.com/tokopedia",false,isChecked = false, isLinkGenerationEnabled),
            AffiliateShareModel("YouTube", IconUnify.YOUTUBE,"youtube",13,sheetType,
                    "Contoh: youtube.com/tokopedia",false,isChecked = false, isLinkGenerationEnabled),
            AffiliateShareModel("Facebook", IconUnify.FACEBOOK,"facebook",1,sheetType,
                    "Contoh: facebook.com/tokopedia",false,isChecked = false, isLinkGenerationEnabled),
            AffiliateShareModel("Twitter", IconUnify.TWITTER,"twitter",10,sheetType,
                    "Contoh: twitter.com/tokopedia",false,isChecked = false, isLinkGenerationEnabled),
            AffiliateShareModel("Website/Blog", IconUnify.GLOBE,"website",11,sheetType,
                    "Contoh: tokopedia.com/tokopedia",false,isChecked = false, isLinkGenerationEnabled))

        if(sheetType == SheetType.ADD_SOCIAL){
            contentView?.findViewById<UnifyButton>(R.id.simpan_btn)?.run {
                show()
                setOnClickListener {
                    onSaveSocialButtonClicked()
                }
            }
        }else {
            (listVisitable as ArrayList<Visitable<AffiliateAdapterTypeFactory>>).add(AffiliateShareModel("WhatsApp", IconUnify.WHATSAPP,"whatsapp",12,sheetType,
                    "",false,isChecked = false, isLinkGenerationEnabled))
            (listVisitable as ArrayList<Visitable<AffiliateAdapterTypeFactory>>).add(AffiliateShareModel("Line", IconUnify.LINE,"line",4,sheetType,
                    "",false, isChecked = false,isLinkGenerationEnabled))
        }

        (listVisitable as ArrayList<Visitable<AffiliateAdapterTypeFactory>>).add(AffiliateShareModel("Lainnya",null,"others", 0,sheetType,
                "Contoh: yourwebiste.com",false, isChecked = false,isLinkGenerationEnabled))
        setSelectedCheckBox()
    }

    private fun setSelectedCheckBox(){
        listVisitable.forEach {
            if(it is AffiliateShareModel && selectedIds.contains(it.id)){
                it.isChecked = true
            }
        }
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
            else if(originScreen == ORIGIN_PERNAH_DIBELI_PROMOSIKA || originScreen == ORIGIN_TERAKHIR_DILIHAT){
                eventCategory = AffiliateAnalytics.CategoryKeys.PROMOSIKAN_BOTTOM_SHEET
            }
            it?.let { data ->
                if(originScreen == ORIGIN_HOME || originScreen == ORIGIN_PROMOSIKAN) {
                    AffiliateAnalytics.sendEvent(
                        AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                        AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK,
                        eventCategory,
                        "$productId-${data.linkID}-$currentServiceFormat",
                        userSessionInterface.userId
                    )
                }
                else if(originScreen == ORIGIN_PERNAH_DIBELI_PROMOSIKA || originScreen == ORIGIN_TERAKHIR_DILIHAT){
                   val eventAction = if(originScreen == ORIGIN_PERNAH_DIBELI_PROMOSIKA){
                        AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_PERNAH_DIABEL
                   } else {
                        AffiliateAnalytics.ActionKeys.CLICK_SALIN_LINK_PERNAH_DILIHAT
                   }
                   AffiliateAnalytics.sendEvent(
                        AffiliateAnalytics.EventKeys.EVENT_VALUE_CLICK,
                        eventAction,
                        eventCategory,
                        "$productId-${data.linkID}-$currentServiceFormat",
                        userSessionInterface.userId
                   )
                }
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
                }else if(originScreen == ORIGIN_PROMOSIKAN) {
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
        }else if(originScreen == ORIGIN_PROMOSIKAN ){
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

    private fun onSaveSocialButtonClicked() {
        val checkedSocialList = arrayListOf<AffiliateShareModel>()
        for (vistitable in listVisitable){
            if(vistitable is AffiliateShareModel && vistitable.isChecked){
                checkedSocialList.add(vistitable)
            }
        }
        affiliatePromotionBottomSheetInterface?.onButtonClick(checkedSocialList)
        dismiss()
    }


    override fun onSocialChecked(position: Int, isChecked : Boolean) {
        (listVisitable[position] as AffiliateShareModel).isChecked = isChecked
        checkForAtLeastOneSelected()
    }

    private fun checkForAtLeastOneSelected() {
        var count = 0
        for (vistitable in listVisitable){
            if((vistitable is AffiliateShareModel) && vistitable.isChecked){ count += 1 }
        }
        if(count == 0) {
            contentView?.findViewById<UnifyButton>(R.id.simpan_btn)?.run {
                buttonVariant = UnifyButton.Variant.GHOST
                buttonType = UnifyButton.Type.ALTERNATE
                isEnabled = false
            }
            contentView?.findViewById<Typography>(R.id.error_message)?.show()
        }
        else {
            contentView?.findViewById<UnifyButton>(R.id.simpan_btn)?.run {
                buttonVariant = UnifyButton.Variant.FILLED
                buttonType = UnifyButton.Type.MAIN
                isEnabled = true
            }
            contentView?.findViewById<Typography>(R.id.error_message)?.hide()
        }
    }
}

interface AffiliatePromotionBottomSheetInterface {
    fun onButtonClick(checkedSocialList: List<AffiliateShareModel>)
}
