package com.tokopedia.tokomember_seller_dashboard.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.di.component.DaggerTokomemberDashComponent
import com.tokopedia.tokomember_seller_dashboard.view.viewmodel.TokomemberDashHomeViewmodel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokomemberDashHomeFragment : BaseDaggerFragment() {

    private var shopId = 0

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private val tokomemberDashHomeViewmodel: TokomemberDashHomeViewmodel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(this, viewModelFactory.get())
        viewModelProvider.get(TokomemberDashHomeViewmodel::class.java)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.tm_dash_home_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        tokomemberDashHomeViewmodel.getHomePageData(6553698, 3827)
    }

    private fun observeViewModel() {

        tokomemberDashHomeViewmodel.tokomemberHomeResultLiveData.observe(viewLifecycleOwner, {
            when(it){
                is Success ->{

                }
                is Fail ->{
                    it.throwable.message
                }
            }
        })
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokomemberDashComponent.builder().build().inject(this)
    }

    companion object {
        fun newInstance(): TokomemberDashHomeFragment {
            return TokomemberDashHomeFragment()
        }
    }
}