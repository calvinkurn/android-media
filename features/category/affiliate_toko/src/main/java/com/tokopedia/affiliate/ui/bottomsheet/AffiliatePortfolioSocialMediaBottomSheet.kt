package com.tokopedia.affiliate.ui.bottomsheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.AFFILIATE_INSTAGRAM_REGEX
import com.tokopedia.affiliate.AFFILIATE_TIKTOK_REGEX
import com.tokopedia.affiliate.AFFILIATE_TWITTER_REGEX
import com.tokopedia.affiliate.AFFILIATE_YT_REGEX
import com.tokopedia.affiliate.adapter.AffiliateAdapter
import com.tokopedia.affiliate.adapter.AffiliateAdapterFactory
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.interfaces.AddSocialInterface
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioButtonData
import com.tokopedia.affiliate.model.pojo.AffiliatePortfolioUrlInputData
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioButtonModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliatePortfolioUrlModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateShareModel
import com.tokopedia.affiliate.viewmodel.AffiliatePortfolioViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AffiliatePortfolioSocialMediaBottomSheet: BottomSheetUnify() , AddSocialInterface {
    private var contentView: View? = null

    private val adapter: AffiliateAdapter = AffiliateAdapter(AffiliateAdapterFactory(addSocialInterface = this))

    @Inject
    lateinit var userSessionInterface : UserSessionInterface

    @Inject
    lateinit var viewModelProvider: ViewModelProvider.Factory

    private val viewModelFragmentProvider by lazy { ViewModelProvider(
        requireParentFragment(), viewModelProvider) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        init()
        return super.onCreateView(inflater, container, savedInstanceState)
    }
    companion object {

        fun newInstance(): AffiliatePortfolioSocialMediaBottomSheet {
            return AffiliatePortfolioSocialMediaBottomSheet()
        }
    }

    private lateinit var portfolioSharedViewModel: AffiliatePortfolioViewModel
    private var selectedIds = arrayListOf<Int>()

    private fun init() {
        portfolioSharedViewModel =  viewModelFragmentProvider.get(AffiliatePortfolioViewModel::class.java)
        selectedIds = portfolioSharedViewModel.getCurrentSocialIds()
        setTitle(getString(R.string.affiliate_add_social_media))
        showCloseIcon = true
        showKnob = false
        contentView = getContentView()
        setDataToRecyclerView()
        initClickListener()
        setChild(contentView)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        initInject()
        return super.onCreateDialog(savedInstanceState)
    }

    private fun initClickListener() {
        contentView?.findViewById<UnifyButton>(R.id.simpan_btn)?.run {
            show()
            setOnClickListener {
                onSaveSocialButtonClicked()
            }
        }
    }

    private fun onSaveSocialButtonClicked() {
        val checkedSocialList = arrayListOf<AffiliateShareModel>()
        for (vistitable in listVisitable){
            if(vistitable is AffiliateShareModel && vistitable.isChecked){
                checkedSocialList.add(vistitable)
            }
        }
        convertToPortfolioModel(checkedSocialList)
        dismiss()
    }

    private fun convertToPortfolioModel(checkedSocialList : List<AffiliateShareModel>) {
        val updateList : java.util.ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
        updateList.add(AffiliateHeaderModel(AffiliateHeaderItemData(userSessionInterface.name,true)))
        for (item in checkedSocialList){
            val portfolioDataItemText = portfolioSharedViewModel.finEditTextModelWithId(item.id)?.text
            if(portfolioDataItemText?.isNotBlank() == true){
                updateList.add(
                    AffiliatePortfolioUrlModel(
                        AffiliatePortfolioUrlInputData(item.id,item.serviceFormat,"${getString(R.string.affiliate_link)} ${item.name}",
                    portfolioDataItemText,item.urlSample,getString(R.string.affiliate_link_not_valid),false,regex = item.regex)
                    )
                )
            }else {
                updateList.add(
                    AffiliatePortfolioUrlModel(
                        AffiliatePortfolioUrlInputData(item.id,item.serviceFormat,"${getString(R.string.affiliate_link)} ${item.name}",
                    "",item.urlSample,getString(R.string.affiliate_link_not_valid),false,regex = item.regex)
                    )
                )
            }
        }
        updateList.add(AffiliatePortfolioButtonModel(AffiliatePortfolioButtonData(getString(R.string.affiliate_tambah_sosial_media), UnifyButton.Type.ALTERNATE, UnifyButton.Variant.GHOST)))
        portfolioSharedViewModel.affiliatePortfolioData.value = updateList
    }

    private fun setDataToRecyclerView() {
        contentView?.findViewById<RecyclerView>(R.id.share_rv)?.let {
            addDataInRecyclerView()
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter.setVisitables(listVisitable)
            it.layoutManager = layoutManager
            it.adapter = adapter
        }
    }

    private var listVisitable: List<Visitable<AffiliateAdapterTypeFactory>> = arrayListOf()
    private fun addDataInRecyclerView() {
        listVisitable = arrayListOf<Visitable<AffiliateAdapterTypeFactory>>(
            AffiliateShareModel("Instagram", IconUnify.INSTAGRAM,"instagram",3,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: instagram.com/tokopedia",false,isChecked = false, false,
                AFFILIATE_INSTAGRAM_REGEX
            ),
            AffiliateShareModel("Tiktok", IconUnify.TIKTOK,"tiktok",9,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: tiktok.com/tokopedia",false,isChecked = false, false,
                AFFILIATE_TIKTOK_REGEX
            ),
            AffiliateShareModel("YouTube", IconUnify.YOUTUBE,"youtube",13,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: youtube.com/tokopedia",false,isChecked = false, false,
                AFFILIATE_YT_REGEX
            ),
            AffiliateShareModel("Facebook", IconUnify.FACEBOOK,"facebook",1,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: facebook.com/tokopedia",false,isChecked = false, false),
            AffiliateShareModel("Twitter", IconUnify.TWITTER,"twitter",10,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: twitter.com/tokopedia",false,isChecked = false, false,
                AFFILIATE_TWITTER_REGEX
            ),
            AffiliateShareModel("Website/Blog", IconUnify.GLOBE,"website",11,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: tokopedia.com/tokopedia",false,isChecked = false, false),
            AffiliateShareModel("Lainnya",null,"others", 0,AffiliatePromotionBottomSheet.Companion.SheetType.ADD_SOCIAL,
                "Contoh: yourwebiste.com",false, isChecked = false,false)
        )
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
        getComponent().injectSocialMediaBottomSheet(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    private fun getContentView(): View {
        return View.inflate(requireContext(), R.layout.affiliate_portfolio_bottom_sheet, null)
    }

    override fun onSocialChecked(position: Int, isChecked: Boolean) {
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