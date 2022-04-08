package com.tokopedia.affiliate

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.pojo.AffiliateHeaderItemData
import com.tokopedia.affiliate.model.pojo.AffiliateTermsAndConditionData
import com.tokopedia.affiliate.ui.fragment.registration.AffiliateTermsAndConditionFragment
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateHeaderModel
import com.tokopedia.affiliate.ui.viewholder.viewmodel.AffiliateTermsAndConditionModel
import com.tokopedia.affiliate_toko.R
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import java.lang.Exception


fun AffiliateTermsAndConditionFragment.createListForTermsAndCondition(context : Context?) : ArrayList<Visitable<AffiliateAdapterTypeFactory>> {
    val itemList : ArrayList<Visitable<AffiliateAdapterTypeFactory>> = ArrayList()
    context?.let {
        itemList.add(AffiliateHeaderModel(AffiliateHeaderItemData(isForPortfolio = false)))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(R.string.affiliate_onboarding_terms_1))))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(R.string.affiliate_onboarding_terms_2))))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(R.string.affiliate_onboarding_terms_3))))
        itemList.add(AffiliateTermsAndConditionModel(AffiliateTermsAndConditionData(context.getString(R.string.affiliate_onboarding_terms_4))))
    }
    return itemList
}
fun View.hideKeyboard(context : Context?){
    try {
        val imm = context?.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager?
        imm?.hideSoftInputFromWindow(this.windowToken, 0)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}