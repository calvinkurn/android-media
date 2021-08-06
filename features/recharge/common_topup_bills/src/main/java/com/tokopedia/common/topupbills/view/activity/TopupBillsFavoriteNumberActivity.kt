package com.tokopedia.common.topupbills.view.activity

import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.header.HeaderUnify
import java.util.ArrayList

class TopupBillsFavoriteNumberActivity : BaseSimpleActivity(), HasComponent<CommonTopupBillsComponent> {

    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var dgCategoryIds: ArrayList<String>
    protected var currentCategoryName = ""
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
            this.clientNumberType = extras.getString(EXTRA_CLIENT_NUMBER_TYPE, "")
            this.number = extras.getString(EXTRA_CLIENT_NUMBER, "")
            this.currentCategoryName = extras.getString(EXTRA_DG_CATEGORY_NAME, "")
            this.dgCategoryIds = extras.getStringArrayList(EXTRA_DG_CATEGORY_IDS) ?: arrayListOf()
            this.operatorData = extras.getParcelable(EXTRA_CATALOG_PREFIX_SELECT)
        }
        super.onCreate(savedInstanceState)
        updateTitle(getString(R.string.common_topup_fav_number_title))

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(com.tokopedia.unifyprinciples.R.color.Unify_Background)

        toolbar.elevation = 0f
        (toolbar as HeaderUnify).transparentMode = false
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return TopupBillsFavoriteNumberFragment
                .newInstance(clientNumberType, number, operatorData, currentCategoryName, dgCategoryIds)
    }

    companion object {
        const val EXTRA_CLIENT_NUMBER_TYPE = "EXTRA_CLIENT_NUMBER_TYPE"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_DG_CATEGORY_NAME = "EXTRA_DG_CATEGORY_NAME"
        const val EXTRA_DG_CATEGORY_IDS = "EXTRA_DG_CATEGORY_IDS"
        const val EXTRA_CATALOG_PREFIX_SELECT = "EXTRA_CATALOG_PREFIX_SELECT"
    }

}