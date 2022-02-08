package com.tokopedia.common.topupbills.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.common.topupbills.CommonTopupBillsComponentInstance
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.prefix_select.TelcoCatalogPrefixSelect
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.fragment.TopupBillsFavoriteNumberFragment
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.SearchBarUnify
import java.util.ArrayList

class TopupBillsFavoriteNumberActivity : BaseSimpleActivity(), HasComponent<CommonTopupBillsComponent> {

    protected lateinit var clientNumberType: String
    protected lateinit var number: String
    protected lateinit var dgCategoryIds: ArrayList<String>
    protected var currentCategoryName = ""
    protected var operatorData: TelcoCatalogPrefixSelect? = null

    private var searchBar: SearchBarUnify? = null
    private var clue: TextView? = null

    var searchBarListener: OnSearchBarListener? = null

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
        initView()
        initListener()

        //draw background without overdraw GPU
        window.setBackgroundDrawableResource(com.tokopedia.unifyprinciples.R.color.Unify_Background)

        toolbar.elevation = 0f
        (toolbar as HeaderUnify).transparentMode = false
    }

    override fun getNewFragment(): androidx.fragment.app.Fragment {
        return TopupBillsFavoriteNumberFragment.newInstance(
            clientNumberType,
            number,
            operatorData,
            currentCategoryName,
            dgCategoryIds
        )
    }

    private fun initView() {
        searchBar = findViewById<SearchBarUnify>(R.id.common_topup_bills_favorite_number_searchbar)
        clue = findViewById<TextView>(R.id.common_topup_bills_favorite_number_clue)
    }

    private fun initListener() {
        searchBar?.searchBarTextField?.addTextChangedListener(getSearchTextWatcher)
    }

    override fun onDestroy() {
        super.onDestroy()
        searchBar?.searchBarTextField?.removeTextChangedListener(getSearchTextWatcher)
    }

    private val getSearchTextWatcher = object : android.text.TextWatcher {
        override fun afterTextChanged(text: android.text.Editable?) {
            text?.let {
                searchBarListener?.setSearchKeyword(text.toString())
            }
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            //do nothing
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            //do nothing
        }
    }

    interface OnSearchBarListener {
        fun setSearchKeyword(keyword: String)
    }

    interface OnEmptyResultListener {
        fun setClueVisibility(isVisible: Boolean)
    }

    companion object {
        fun createInstance(
            context: Context,
            clientNumber: String,
            dgCategoryIds: ArrayList<String>,
            categoryName: String,
        ): Intent {
            val intent = Intent(context, TopupBillsFavoriteNumberActivity::class.java)
            val extras = Bundle()
            extras.putString(EXTRA_CLIENT_NUMBER_TYPE, ClientNumberType.TYPE_INPUT_TEL.value)
            extras.putString(EXTRA_CLIENT_NUMBER, clientNumber)
            extras.putStringArrayList(EXTRA_DG_CATEGORY_IDS, dgCategoryIds)
            extras.putString(EXTRA_DG_CATEGORY_NAME, categoryName)

            intent.putExtras(extras)
            return intent
        }

        const val EXTRA_CLIENT_NUMBER_TYPE = "EXTRA_CLIENT_NUMBER_TYPE"
        const val EXTRA_CLIENT_NUMBER = "EXTRA_CLIENT_NUMBER"
        const val EXTRA_DG_CATEGORY_NAME = "EXTRA_DG_CATEGORY_NAME"
        const val EXTRA_DG_CATEGORY_IDS = "EXTRA_DG_CATEGORY_IDS"
        const val EXTRA_CATALOG_PREFIX_SELECT = "EXTRA_CATALOG_PREFIX_SELECT"
    }

}