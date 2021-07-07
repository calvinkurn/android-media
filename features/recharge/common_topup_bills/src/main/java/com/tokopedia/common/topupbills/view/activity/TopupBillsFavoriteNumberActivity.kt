package com.tokopedia.common.topupbills.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.prefix_select.RechargeCatalogPrefixSelect
import com.tokopedia.common.topupbills.data.prefix_select.TelcoAttributesOperator
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.header.HeaderUnify
import java.util.ArrayList
import kotlin.properties.Delegates

class TopupBillsFavoriteNumberActivity : BaseSimpleActivity(), HasComponent<CommonTopupBillsComponent> {

    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var dgCategoryIds: ArrayList<String>
    protected var currentCategoryId by Delegates.notNull<Int>()
    protected var operatorData: TelcoCatalogPrefixSelect? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_digital_search_number_rev
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
        return TopupBillsFavoriteNumberActivity::class.java.simpleName
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        extras?.let {
            this.clientNumberType = extras.getString(EXTRA_CLIENT_NUMBER, "")
            this.number = extras.getString(EXTRA_NUMBER, "")
            this.currentCategoryId = extras.getInt(EXTRA_DG_CATEGORY_ID, 0)
            this.dgCategoryIds = extras.getStringArrayList(EXTRA_DG_CATEGORY_IDS) ?: arrayListOf()
            this.operatorData = extras.getParcelable(EXTRA_CATALOG_PREFIX_SELECT)
        }
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.common_topup_title_fav_number))

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(com.tokopedia.unifyprinciples.R.color.Unify_Background)

        toolbar.elevation = 0f
        (toolbar as HeaderUnify).transparentMode = false
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return TopupBillsFavoriteNumberFragment
                .newInstance(clientNumberType, number, operatorData, currentCategoryId, dgCategoryIds)
    }

    companion object {

        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_NUMBER = "EXTRA_NUMBER"
        const val EXTRA_DG_CATEGORY_ID = "EXTRA_DG_CATEGORY_ID"
        const val EXTRA_DG_CATEGORY_IDS = "EXTRA_DG_CATEGORY_IDS"
        const val EXTRA_CATALOG_PREFIX_SELECT = "EXTRA_CATALOG_PREFIX_SELECT"

        fun getCallingIntent(context: Context, clientNumberType: String,
                             number: String, catalogPrefixSelect: TelcoCatalogPrefixSelect,
                             currentCategoryId: Int, digitalCategoryIds: ArrayList<String>
        ): Intent {
            val intent = Intent(context, TopupBillsFavoriteNumberActivity::class.java)
            val extras = Bundle()
            extras.putString(EXTRA_CLIENT_NUMBER, clientNumberType)
            extras.putString(EXTRA_NUMBER, number)
            extras.putStringArrayList(EXTRA_DG_CATEGORY_IDS, digitalCategoryIds)
            extras.putInt(EXTRA_DG_CATEGORY_ID, currentCategoryId)
            extras.putParcelable(EXTRA_CATALOG_PREFIX_SELECT, catalogPrefixSelect)
            intent.putExtras(extras)
            return intent
        }
    }

}