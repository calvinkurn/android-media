package com.tokopedia.settingnotif.usersetting.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.util.CacheManager.KEY_ONBOARDING
import com.tokopedia.settingnotif.usersetting.util.CacheManager.getCacheBoolean
import com.tokopedia.settingnotif.usersetting.util.CacheManager.saveCacheBoolean
import com.tokopedia.settingnotif.usersetting.view.activity.UserNotificationSettingActivity
import com.tokopedia.settingnotif.usersetting.view.adapter.SettingTypeAdapter
import com.tokopedia.settingnotif.usersetting.view.dataview.SettingTypeDataView
import com.tokopedia.settingnotif.usersetting.widget.NotifSettingDividerDecoration
import java.util.*

class SettingTypeFragment : BaseDaggerFragment() {

    private var rvSettingType: RecyclerView? = null
    private var settingTypeContract: SettingTypeContract? = null

    private var coachMark: CoachMark2? = null

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
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindView(view)

        setupToolbar()
        setupSettingTypes()
        showShowCaseIfNeeded()
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
        rvSettingType?.let {
            it.setHasFixedSize(true)
            it.adapter = SettingTypeAdapter(
                    SettingTypeDataView.createSettingTypes(),
                    settingTypeContract
            )
            it.addItemDecoration(NotifSettingDividerDecoration(requireContext()))
        }
    }

    private fun showShowCaseIfNeeded() {
        var skipShowCase = false
        activity?.intent?.data?.let {
            skipShowCase = it.getQueryParameter(UserNotificationSettingActivity.PUSH_NOTIFICATION_PAGE) != null
        }
        val hasBeenShown = getCacheBoolean(context, KEY_ONBOARDING)
        if (hasBeenShown || skipShowCase) return

        showShowCase()
    }

    private fun showShowCase() {
        context?.let {
            coachMark = CoachMark2(it)
            coachMark?.showCoachMark(generateShowCaseList())
            coachMark?.onFinishListener = { saveCacheBoolean(it, KEY_ONBOARDING, true) }
            coachMark?.onDismissListener = { saveCacheBoolean(it, KEY_ONBOARDING, true) }
        }
    }

    private fun generateShowCaseList(): ArrayList<CoachMark2Item> {
        return arrayListOf(generateShowCaseSettingObject())
    }

    private fun generateShowCaseSettingObject(): CoachMark2Item {
        val title = getString(R.string.title_show_case_setting_type)
        val description = getString(R.string.description_show_case_setting_type)
        val position = CoachMark2.POSITION_BOTTOM
        return CoachMark2Item(rvSettingType!!, title, description, position)
    }

    override fun getScreenName() = getString(R.string.settingnotif_title)

    override fun initInjector() {}

    companion object {
        private const val ILLEGAL_EXCEPTION_MESSAGE = "The activity must implement SettingTypeContract interface"
        private const val BROADCAST_MESSAGE = ".BroadcastMessage"

        fun createInstance(bundle: Bundle? = Bundle()): Fragment {
            val fragment = SettingTypeFragment()
            fragment.arguments = bundle ?: Bundle()
            return fragment
        }
    }

}