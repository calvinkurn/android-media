package com.tokopedia.sellerhome.settings.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.RouteManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.di.DaggerOtherSettingComponent
import com.tokopedia.sellerhome.settings.view.activity.MenuSettingActivity
import com.tokopedia.sellerhome.settings.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.MenuItemUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.OtherMenuViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherMenuViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_other_menu.*
import javax.inject.Inject

class OtherMenuFragment: BaseListFragment<SettingUiModel, OtherMenuAdapterTypeFactory>() {

    companion object {
        private const val PENGATURAN = "Pengaturan"
        @JvmStatic
        fun createInstance(): OtherMenuFragment = OtherMenuFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var otherMenuViewHolder: OtherMenuViewHolder? = null

    private val otherSettingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OtherMenuViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherSettingViewModel.populateAdapterList()
    }

    override fun onResume() {
        super.onResume()
        getAllShopInfoData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        observeLiveData()
    }

    override fun getAdapterTypeFactory(): OtherMenuAdapterTypeFactory = OtherMenuAdapterTypeFactory()

    override fun onItemClicked(settingUiModel: SettingUiModel) {
        settingUiModel.onClickApplink?.let {
            RouteManager.route(context, it)
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerOtherSettingComponent.builder()
                .baseAppComponent((requireContext().applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)
    }

    override fun loadData(page: Int) {

    }

    private fun observeLiveData() {
        with(otherSettingViewModel) {
            settingListLiveData.observe(viewLifecycleOwner, Observer { settingList ->
                populateAdapterData(settingList)
            })
            generalShopInfoLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showGeneralShopInfoSuccess(result.data)
                    is Fail -> showGeneralShopInfoError()
                }
            })
            shopBadgeLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showShopBadgeSuccess(result.data)
                    is Fail -> showShopBadgeError()
                }
            })
            totalFollowersLiveData.observe(viewLifecycleOwner, Observer { result ->
                when(result) {
                    is Success -> showTotalFollowingSuccess(result.data)
                    is Fail -> showTotalFollowingError()
                }
            })
        }
    }

    private fun populateAdapterData(settingList: List<SettingUiModel>) {
        val fullSettingList = settingList.toMutableList()
        val goToMenuSetting : () -> Unit = {
            startActivity(Intent(context, MenuSettingActivity::class.java))
        }
        fullSettingList.add(
                MenuItemUiModel(PENGATURAN, R.drawable.ic_setting, null, goToMenuSetting))
        adapter.data.addAll(fullSettingList)
        adapter.notifyDataSetChanged()
        renderList(fullSettingList)
    }

    private fun getAllShopInfoData() {
        showAllLoadingShimmering()
        otherSettingViewModel.getAllSettingShopInfo()
    }

    private fun showAllLoadingShimmering() {
        showGeneralShopInfoLoading()
        showShopBadgeLoading()
        showTotalFollowingLoading()
    }

    private fun showGeneralShopInfoSuccess(generalShopInfoUiModel: GeneralShopInfoUiModel) {
        generalShopInfoUiModel.run {
            otherMenuViewHolder?.onSuccessGetShopGeneralInfoData(this)
        }
    }

    private fun showShopBadgeSuccess(shopBadgeUrl: String) {
        otherMenuViewHolder?.onSuccessGetShopBadge(shopBadgeUrl)
    }

    private fun showTotalFollowingSuccess(totalFollowers: Int) {
        otherMenuViewHolder?.onSuccessGetTotalFollowing(totalFollowers)
    }

    private fun showGeneralShopInfoLoading() {
        otherMenuViewHolder?.onLoadingGetShopGeneralInfoData()
    }

    private fun showShopBadgeLoading() {
        otherMenuViewHolder?.onLoadingGetShopBadge()
    }

    private fun showTotalFollowingLoading() {
        otherMenuViewHolder?.onLoadingGetTotalFollowing()
    }

    private fun showGeneralShopInfoError() {

    }

    private fun showShopBadgeError() {

    }

    private fun showTotalFollowingError() {

    }

    private fun setupView(view: View) {
        recycler_view.layoutManager = LinearLayoutManager(context)
        context?.let { otherMenuViewHolder = OtherMenuViewHolder(view, it)}
        otherMenuViewHolder?.initBindView()
    }

}