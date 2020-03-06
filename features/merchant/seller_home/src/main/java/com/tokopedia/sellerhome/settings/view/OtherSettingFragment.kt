package com.tokopedia.sellerhome.settings.view

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
import com.tokopedia.sellerhome.settings.view.typefactory.OtherSettingAdapterTypeFactory
import com.tokopedia.sellerhome.settings.view.uimodel.GeneralShopInfoUiModel
import com.tokopedia.sellerhome.settings.view.uimodel.base.PowerMerchantStatus
import com.tokopedia.sellerhome.settings.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.settings.view.viewholder.ShopInfoViewHolder
import com.tokopedia.sellerhome.settings.view.viewmodel.OtherSettingViewModel
import kotlinx.android.synthetic.main.fragment_other_setting.*
import javax.inject.Inject

class OtherSettingFragment: BaseListFragment<SettingUiModel, OtherSettingAdapterTypeFactory>() {

    companion object {
        @JvmStatic
        fun createInstance(): OtherSettingFragment = OtherSettingFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private var shopInfoViewHolder: ShopInfoViewHolder? = null

    private val otherSettingViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(OtherSettingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otherSettingViewModel.populateAdapterList()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_other_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        observeLiveData()
    }

    override fun getAdapterTypeFactory(): OtherSettingAdapterTypeFactory = OtherSettingAdapterTypeFactory()

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
        otherSettingViewModel.settingListLiveData.observe(viewLifecycleOwner, Observer { settingList ->
            populateAdapterData(settingList)
        })
    }

    private fun populateAdapterData(settingList: List<SettingUiModel>) {
        adapter.data.addAll(settingList)
        adapter.notifyDataSetChanged()
        renderList(settingList)
        shopInfoViewHolder?.onSuccessGetShopGeneralInfoData(GeneralShopInfoUiModel("Adeedast Naiki", "https://www.bukalapak.com/images/logo-google-graph.png", PowerMerchantStatus.Active))
    }

    private fun setupView(view: View) {
        recycler_view.layoutManager = LinearLayoutManager(context)
        context?.let { shopInfoViewHolder = ShopInfoViewHolder(view, it)}
    }

}