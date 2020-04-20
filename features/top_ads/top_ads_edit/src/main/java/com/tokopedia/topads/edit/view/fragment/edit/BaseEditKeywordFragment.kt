package com.tokopedia.topads.edit.view.fragment.edit

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.data.SharedViewModel
import com.tokopedia.topads.edit.data.param.NegKeyword
import com.tokopedia.topads.edit.data.response.GetKeywordResponse
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.view.activity.EditFormAdActivityCallback
import com.tokopedia.topads.edit.view.activity.SaveButtonStateCallBack
import com.tokopedia.topads.edit.view.adapter.KeywordEditPagerAdapter
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_edit_keyword_base_layout.*
import javax.inject.Inject

class BaseEditKeywordFragment : BaseDaggerFragment(), EditKeywordsFragment.ButtonAction {

    @Inject
    lateinit var viewModel: EditFormDefaultViewModel

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var callback: EditFormAdActivityCallback? = null
    private var buttonStateCallback: SaveButtonStateCallBack? = null
    lateinit var sharedViewModel: SharedViewModel
    private var btnState = true

    companion object {
        private const val POSITIVE_KEYWORDS = "positive_keywords"
        private const val NEGATIVE_KEYWORDS = "negative_keywords"
        private const val POSITIVE_CREATE = "createdPositiveKeyword"
        private const val POSITIVE_DELETE = "deletedPositiveKeyword"
        private const val POSITIVE_EDIT = "editedPositiveKeyword"


        fun newInstance(bundle: Bundle?): BaseEditKeywordFragment {
            val fragment = BaseEditKeywordFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EditFormDefaultViewModel::class.java)

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel::class.java)
        return inflater.inflate(resources.getLayout(R.layout.topads_edit_keyword_base_layout), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keyword.isChecked = true
        renderViewPager()
        keyword_grp.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.keyword) {
                view_pager.currentItem = 0

            } else {
                view_pager.currentItem = 1
            }

        }
    }

    private fun renderViewPager() {
        view_pager.adapter = getViewPagerAdapter()
        view_pager.disableScroll(true)
    }

    private fun getViewPagerAdapter(): KeywordEditPagerAdapter? {
        val list: ArrayList<Fragment> = arrayListOf()
        list.add(EditKeywordsFragment())
        list.add(EditNegativeKeywordsFragment())
        val adapter = KeywordEditPagerAdapter(childFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    fun getButtonState(): Boolean {
        return btnState
    }

    override fun onAttach(context: Context) {
        if (context is EditFormAdActivityCallback) {
            callback = context
        }
        if (context is SaveButtonStateCallBack) {
            buttonStateCallback = context
        }
        super.onAttach(context)
    }

    override fun onDetach() {
        callback = null
        buttonStateCallback = null
        super.onDetach()
    }

    override fun buttonDisable(enable: Boolean) {
        btnState = enable
        buttonStateCallback?.setButtonState()
    }

    override fun getScreenName(): String {
        return BaseEditKeywordFragment::class.java.name
    }

    override fun initInjector() {
        getComponent(TopAdsEditComponent::class.java).inject(this)
    }

    fun sendData(): HashMap<String, Any?> {
        val dataMap = HashMap<String, Any?>()
        val fragments = (view_pager.adapter as KeywordEditPagerAdapter).list
        var dataNegative: ArrayList<NegKeyword>? = arrayListOf()
        var deletedKeywordsPos: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var addedKeywordsPos: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()
        var editedKeywordsPos: ArrayList<GetKeywordResponse.KeywordsItem>? = arrayListOf()

        if (fragments[0] is EditKeywordsFragment) {
            var bundle: Bundle? = null
            bundle = (fragments[0] as EditKeywordsFragment).sendData()
            addedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_CREATE)
            deletedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_DELETE)
            editedKeywordsPos = bundle.getParcelableArrayList(POSITIVE_EDIT)

        }
        if (fragments[1] is EditNegativeKeywordsFragment) {
            var bundle: Bundle? = null
            bundle = (fragments[1] as EditNegativeKeywordsFragment).sendData()
            dataNegative = bundle.getParcelableArrayList(NEGATIVE_KEYWORDS)
        }
        dataMap[POSITIVE_CREATE] = addedKeywordsPos
        dataMap[POSITIVE_DELETE] = deletedKeywordsPos
        dataMap[POSITIVE_EDIT] = editedKeywordsPos
        dataMap[NEGATIVE_KEYWORDS] = dataNegative
        return dataMap
    }

}