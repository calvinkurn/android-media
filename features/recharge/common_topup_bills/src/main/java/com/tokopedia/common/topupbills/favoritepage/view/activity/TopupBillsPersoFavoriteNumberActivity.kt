package com.tokopedia.common.topupbills.favoritepage.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.favoritepage.view.fragment.SingleTabSavedNumberFragment
import com.tokopedia.common.topupbills.favoritepage.view.util.FavoriteNumberPageConfig
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.header.HeaderUnify
import java.util.ArrayList
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class TopupBillsPersoFavoriteNumberActivity :
    BaseSimpleActivity(),
    HasComponent<CommonTopupBillsComponent> {

    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var dgCategoryIds: ArrayList<String>
    protected lateinit var dgOperatorIds: ArrayList<String>
    protected var currentCategoryName = ""
    protected var loyaltyStatus: String = ""
    protected var pageConfig: FavoriteNumberPageConfig = FavoriteNumberPageConfig.TELCO

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_favorite_number
    }

    override fun getParentViewResourceID(): Int {
        return R.id.parent_view
    }

    override fun getComponent(): CommonTopupBillsComponent {
        return CommonTopupBillsComponentInstance.getCommonTopupBillsComponent(application)
    }

    override fun getToolbarResourceID(): Int {
        return R.id.toolbar
    }

    override fun getScreenName(): String? {
        return TopupBillsPersoFavoriteNumberActivity::class.java.simpleName
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        extras?.let {
            this.clientNumberType = extras.getString(EXTRA_CLIENT_NUMBER_TYPE, "")
            this.number = extras.getString(EXTRA_CLIENT_NUMBER, "")
            this.currentCategoryName = extras.getString(EXTRA_DG_CATEGORY_NAME, "")
            this.dgCategoryIds = extras.getStringArrayList(EXTRA_DG_CATEGORY_IDS) ?: arrayListOf()
            this.dgOperatorIds = extras.getStringArrayList(EXTRA_DG_OPERATOR_IDS) ?: arrayListOf()
            this.loyaltyStatus = extras.getString(EXTRA_LOYALTY_STATUS, "")

            val favoriteNumberPageConfig = extras
                .getSerializable(EXTRA_FAVORITE_NUMBER_PAGE_CONFIG) as? FavoriteNumberPageConfig
            if (favoriteNumberPageConfig != null) {
                pageConfig = favoriteNumberPageConfig
            }
        }
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.common_topup_fav_number_title))

        // draw background without overdraw GPU
        window.setBackgroundDrawableResource(unifyprinciplesR.color.Unify_Background)

        toolbar.elevation = 0f
        (toolbar as HeaderUnify).transparentMode = false
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return SingleTabSavedNumberFragment.newInstance(
            clientNumberType,
            number,
            currentCategoryName,
            dgCategoryIds,
            dgOperatorIds,
            loyaltyStatus,
            pageConfig
        )
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, null)
        super.onBackPressed()
    }

    companion object {
        fun createInstance(
            context: Context,
            clientNumber: String,
            dgCategoryIds: ArrayList<String>,
            dgOperatorIds: ArrayList<String>,
            categoryName: String,
            loyaltyStatus: String,
            favoriteNumberPageConfig: FavoriteNumberPageConfig = FavoriteNumberPageConfig.TELCO
        ): Intent {
            val intent = Intent(context, TopupBillsPersoFavoriteNumberActivity::class.java)
            val extras = Bundle()
            extras.putString(EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL.value)
            extras.putString(EXTRA_CLIENT_NUMBER, clientNumber)
            extras.putStringArrayList(EXTRA_DG_CATEGORY_IDS, dgCategoryIds)
            extras.putStringArrayList(EXTRA_DG_OPERATOR_IDS, dgOperatorIds)
            extras.putString(EXTRA_LOYALTY_STATUS, loyaltyStatus)
            extras.putString(EXTRA_DG_CATEGORY_NAME, categoryName)
            extras.putSerializable(EXTRA_FAVORITE_NUMBER_PAGE_CONFIG, favoriteNumberPageConfig)
            intent.putExtras(extras)
            return intent
        }

        const val EXTRA_CLIENT_NUMBER_TYPE = "EXTRA_CLIENT_NUMBER_TYPE"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_DG_CATEGORY_NAME = "EXTRA_DG_CATEGORY_NAME"
        const val EXTRA_DG_CATEGORY_IDS = "EXTRA_DG_CATEGORY_IDS"
        const val EXTRA_DG_OPERATOR_IDS = "EXTRA_DG_OPERATOR_IDS"
        const val EXTRA_LOYALTY_STATUS = "EXTRA_LOYALTY_STATUS"
        const val EXTRA_FAVORITE_NUMBER_PAGE_CONFIG = "EXTRA_FAVORITE_NUMBER_PAGE_CONFIG"
    }
}
