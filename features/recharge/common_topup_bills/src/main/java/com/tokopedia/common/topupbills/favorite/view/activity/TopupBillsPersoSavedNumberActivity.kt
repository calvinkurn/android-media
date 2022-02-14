package com.tokopedia.common.topupbills.favorite.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.favorite.view.fragment.DualTabSavedNumberFragment
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.header.HeaderUnify
import java.util.ArrayList

class TopupBillsPersoSavedNumberActivity: BaseSimpleActivity(),
    HasComponent<CommonTopupBillsComponent> {

    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var dgCategoryIds: ArrayList<String>
    protected var currentCategoryName = ""
    protected var operatorData: TelcoCatalogPrefixSelect? = null
    protected var isSwitchChecked = false

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_saved_number
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
        return TopupBillsPersoSavedNumberActivity::class.java.simpleName
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        extras?.let {
            this.clientNumberType = extras.getString(EXTRA_CLIENT_NUMBER_TYPE, "")
            this.number = extras.getString(EXTRA_CLIENT_NUMBER, "")
            this.currentCategoryName = extras.getString(EXTRA_DG_CATEGORY_NAME, "")
            this.dgCategoryIds = extras.getStringArrayList(EXTRA_DG_CATEGORY_IDS) ?: arrayListOf()
            this.operatorData = extras.getParcelable(EXTRA_CATALOG_PREFIX_SELECT)
            this.isSwitchChecked = extras.getBoolean(EXTRA_IS_SWITCH_CHECKED, false)
        }
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.common_topup_saved_number_title))

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(com.tokopedia.unifyprinciples.R.color.Unify_Background)

        toolbar.elevation = 0f
        (toolbar as HeaderUnify).transparentMode = false
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return DualTabSavedNumberFragment
            .newInstance(clientNumberType, number, operatorData,
                currentCategoryName, dgCategoryIds, isSwitchChecked)
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
            categoryName: String,
            operatorData: TelcoCatalogPrefixSelect,
            isSwitchChecked: Boolean
        ): Intent {
            val intent = Intent(context, TopupBillsPersoSavedNumberActivity::class.java)
            val extras = Bundle()
            extras.putString(EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL.value)
            extras.putString(EXTRA_CLIENT_NUMBER, clientNumber)
            extras.putStringArrayList(EXTRA_DG_CATEGORY_IDS, dgCategoryIds)
            extras.putString(EXTRA_DG_CATEGORY_NAME, categoryName)
            extras.putParcelable(EXTRA_CATALOG_PREFIX_SELECT, operatorData)
            extras.putBoolean(EXTRA_IS_SWITCH_CHECKED, isSwitchChecked)

            intent.putExtras(extras)
            return intent
        }
        const val EXTRA_CLIENT_NUMBER_TYPE = "EXTRA_CLIENT_NUMBER_TYPE"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_DG_CATEGORY_NAME = "EXTRA_DG_CATEGORY_NAME"
        const val EXTRA_DG_CATEGORY_IDS = "EXTRA_DG_CATEGORY_IDS"
        const val EXTRA_CATALOG_PREFIX_SELECT = "EXTRA_CATALOG_PREFIX_SELECT"
        const val EXTRA_IS_SWITCH_CHECKED = "EXTRA_IS_SWITCH_CHECKED"

        const val EXTRA_CALLBACK_CLIENT_NUMBER = "EXTRA_CALLBACK_CLIENT_NUMBER"
    }
}