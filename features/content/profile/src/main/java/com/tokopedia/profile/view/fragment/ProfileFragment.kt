package com.tokopedia.profile.view.fragment

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactoryImpl
import com.tokopedia.profile.view.listener.ProfileContract

/**
 * @author by milhamj on 9/17/18.
 */
class ProfileFragment : BaseListFragment<Visitable<*>, BaseAdapterTypeFactory>(),
        ProfileContract.View {

    companion object {
        fun createInstance() = ProfileFragment()
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        return LayoutInflater.from(context).inflate(R.layout.profile_fragment, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//    }

    override fun getScreenName(): String? = null

    override fun initInjector() {

    }

    override fun getAdapterTypeFactory(): BaseAdapterTypeFactory {
        return ProfileTypeFactoryImpl(this)
    }

    override fun onItemClicked(t: Visitable<*>?) {
    }

    override fun loadData(page: Int) {
    }
}