package com.tokopedia.common.topupbills.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.di.DaggerCommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.header.HeaderUnify
import java.util.ArrayList

class TopupBillsFavoriteNumberActivity : BaseSimpleActivity(), HasComponent<CommonTopupBillsComponent> {

    protected lateinit var categoryId: String
    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var numberList: List<TopupBillsFavNumberItem>

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

    override fun onCreate(savedInstanceState: Bundle?) {
        val extras = intent.extras
        extras?.let {
            this.clientNumberType = extras.getString(EXTRA_CLIENT_NUMBER, "")
            this.number = extras.getString(EXTRA_NUMBER, "")
            this.numberList = extras.getParcelableArrayList(EXTRA_NUMBER_LIST) ?: listOf()
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
                .newInstance(clientNumberType, number, numberList)
    }

    companion object {

        const val EXTRA_NUMBER_LIST = "EXTRA_NUMBER_LIST"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_NUMBER = "EXTRA_NUMBER"
        const val EXTRA_CATEGORY_ID = "EXTRA_CATEGORY_ID"

        const val EXTRA_CALLBACK_CLIENT_NUMBER = "EXTRA_CALLBACK_CLIENT_NUMBER"
        const val EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE = "EXTRA_CALLBACK_INPUT_NUMBER_ACTION_TYPE"

        fun getCallingIntent(context: Context, clientNumberType: String,
                             number: String, numberList: List<TopupBillsFavNumberItem>): Intent {
            val intent = Intent(context, TopupBillsFavoriteNumberActivity::class.java)
            intent.putExtra(EXTRA_CLIENT_NUMBER, clientNumberType)
            intent.putExtra(EXTRA_NUMBER, number)
            intent.putParcelableArrayListExtra(EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            return intent
        }
    }

}