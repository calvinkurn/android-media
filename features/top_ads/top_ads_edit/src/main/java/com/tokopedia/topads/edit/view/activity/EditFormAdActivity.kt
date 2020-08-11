package com.tokopedia.topads.edit.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.topads.edit.R
import com.tokopedia.topads.edit.di.DaggerTopAdsEditComponent
import com.tokopedia.topads.edit.di.TopAdsEditComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.utils.Constants.TAB_POSITION
import com.tokopedia.topads.edit.view.adapter.TopAdsEditPagerAdapter
import com.tokopedia.topads.edit.view.fragment.edit.BaseEditKeywordFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditGroupAdFragment
import com.tokopedia.topads.edit.view.fragment.edit.EditProductFragment
import com.tokopedia.topads.edit.view.model.EditFormDefaultViewModel
import kotlinx.android.synthetic.main.topads_base_edit_activity_layout.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class EditFormAdActivity : BaseActivity(), HasComponent<TopAdsEditComponent>, SaveButtonStateCallBack {

    @Inject
    lateinit var viewModel: EditFormDefaultViewModel
    private lateinit var adapter: TopAdsEditPagerAdapter
    var list: ArrayList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.topads_base_edit_activity_layout)
        renderTabAndViewPager()
        setupToolBar()
        backArrow.setOnClickListener {
            onBackPressed()
        }

        btn_submit.setOnClickListener {
            showConfirmationDialog(true)
        }
    }

    private fun getDataFromChildFragments() {
        val fragments = (view_pager.adapter as TopAdsEditPagerAdapter).list
        var dataProduct = Bundle()
        var dataKeyword = HashMap<String, Any?>()
        var dataGroup = HashMap<String, Any?>()
        for (fragment in fragments) {
            when (fragment) {
                is EditProductFragment ->
                    dataProduct = fragment.sendData()

                is EditGroupAdFragment ->
                    dataGroup = fragment.sendData()

                is BaseEditKeywordFragment ->
                    dataKeyword = fragment.sendData()
            }
        }

        viewModel.topAdsCreated(dataProduct, dataKeyword, dataGroup,
                ::onSuccessGroupEdited, ::finish)
    }

    private fun onSuccessGroupEdited() {
        val returnIntent = Intent()
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }

    private fun setupToolBar() {
        setSupportActionBar(toolbar)
    }

    private fun renderTabAndViewPager() {
        val bundle = intent.extras
        view_pager.adapter = getViewPagerAdapter()
        view_pager.offscreenPageLimit = 3
        view_pager.currentItem = bundle?.getInt(TAB_POSITION, 2) ?: 2
        view_pager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.setupWithViewPager(view_pager)
    }

    private fun getViewPagerAdapter(): TopAdsEditPagerAdapter {
        val bundle = intent.extras
        val list: ArrayList<Fragment> = ArrayList()
        list.add(EditProductFragment.newInstance(bundle))
        list.add(BaseEditKeywordFragment.newInstance(bundle))
        list.add(EditGroupAdFragment.newInstance(bundle))

        adapter = TopAdsEditPagerAdapter(supportFragmentManager, 0)
        adapter.setData(list)
        return adapter
    }

    override fun getComponent(): TopAdsEditComponent {
        val toAdsEditComponent = DaggerTopAdsEditComponent.builder().baseAppComponent(
                (application as BaseMainApplication).baseAppComponent).topAdEditModule(TopAdEditModule(this)).build()
        toAdsEditComponent.inject(this)
        return toAdsEditComponent
    }

    override fun onBackPressed() {
        showConfirmationDialog(false)
    }

    private fun showConfirmationDialog(dismiss: Boolean) {
        val dialog = DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
        dialog.setTitle(getString(R.string.topads_edit_leave_page_conf_dialog_title))
        dialog.setDescription(getString(R.string.topads_edit_leave_page_conf_dialog_desc))
        dialog.setPrimaryCTAText(getString(R.string.simpan))
        dialog.setSecondaryCTAText(getString(R.string.topads_edit_keluar))
        dialog.setPrimaryCTAClickListener {
            getDataFromChildFragments()
        }
        dialog.setSecondaryCTAClickListener {
            dialog.dismiss()
            if (!dismiss)
                super.onBackPressed()
        }
        dialog.show()
    }

    override fun setButtonState() {
        val fragments = (view_pager.adapter as TopAdsEditPagerAdapter).list
        var valid1 = true
        var valid2 = true
        var valid3 = true
        for (fragment in fragments) {
            when (fragment) {
                is EditProductFragment -> {
                    valid1 = fragment.getButtonState()
                }
                is EditGroupAdFragment -> {
                    valid2 = fragment.getButtonState()
                }
                is BaseEditKeywordFragment -> {
                    valid3 = fragment.getButtonState()
                }
            }
            btn_submit.isEnabled = valid1 && valid2 && valid3
        }
    }
}

interface SaveButtonStateCallBack {
    fun setButtonState()
}

