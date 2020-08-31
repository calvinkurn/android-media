package com.tokopedia.settingnotif.usersetting.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.const.Unify.Neutral_N700_96
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingTypeAdapter
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingTypeDataView
import com.tokopedia.showcase.*
import java.util.*

class SettingTypeFragment : BaseDaggerFragment() {

    private lateinit var rvSettingType: RecyclerView
    private lateinit var settingTypeContract: SettingTypeContract

    interface SettingTypeContract {
        fun openSettingField(settingType: SettingTypeDataView)
    }

    override fun onAttachActivity(context: Context?) {
        if (context is SettingTypeContract) {
            settingTypeContract = context
        } else {
            throw IllegalStateException(ILLEGAL_EXCEPTION_MESSAGE)
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return LayoutInflater.from(context).inflate(
                R.layout.fragment_setting_type,
                container,
                false
        ).also {
            bindView(it)
            setupToolbar()
            setupSettingTypes()
            showShowCaseIfNeeded()
        }
    }

    private fun bindView(view: View) {
        rvSettingType = view.findViewById(R.id.rv_setting_type)
    }

    private fun setupToolbar() {
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = screenName
        }
    }

    private fun setupSettingTypes() {
        with (rvSettingType) {
            setHasFixedSize(true)
            adapter = SettingTypeAdapter(
                    SettingTypeDataView.createSettingTypes(),
                    settingTypeContract
            )
        }
    }

    private fun showShowCaseIfNeeded() {
        val tag = javaClass.name + BROADCAST_MESSAGE
        val hasBeenShown = ShowCasePreference.hasShown(context, tag)
        if (hasBeenShown) return
        showShowCase(tag)
    }

    private fun showShowCase(tag: String) {
        val dialog = generateShowCaseDialog()
        val showCaseList = generateShowCaseList()
        dialog.show(activity, tag, showCaseList)
    }

    private fun generateShowCaseDialog(): ShowCaseDialog {
        return ShowCaseBuilder()
                .backgroundContentColorRes(Neutral_N700_96)
                .shadowColorRes(R.color.shadow)
                .textColorRes(R.color.grey_400)
                .textSizeRes(com.tokopedia.unifyprinciples.R.dimen.fontSize_lvl2)
                .titleTextSizeRes(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4)
                .clickable(true)
                .useArrow(true)
                .build()
    }

    private fun generateShowCaseList(): ArrayList<ShowCaseObject> {
        return arrayListOf(generateShowCaseSettingObject())
    }

    private fun generateShowCaseSettingObject(): ShowCaseObject {
        val title = getString(R.string.title_show_case_setting_type)
        val description = getString(R.string.description_show_case_setting_type)
        val position = ShowCaseContentPosition.BOTTOM
        return ShowCaseObject(rvSettingType, title, description, position)
    }

    override fun getScreenName() = getString(R.string.settingnotif_title)

    override fun initInjector() {}

    companion object {
        private const val ILLEGAL_EXCEPTION_MESSAGE = "The activity must implement SettingTypeContract interface"
        private const val BROADCAST_MESSAGE = ".BroadcastMessage"
    }

}