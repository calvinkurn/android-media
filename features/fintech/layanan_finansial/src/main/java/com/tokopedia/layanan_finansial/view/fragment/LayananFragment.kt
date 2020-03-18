package com.tokopedia.layanan_finansial.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.di.LayananComponent
import com.tokopedia.layanan_finansial.view.customview.LayananSectionView
import com.tokopedia.layanan_finansial.view.models.LayananFinansialModel
import com.tokopedia.layanan_finansial.view.viewModel.LayananFinansialViewModel
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.layanan_fragment.*
import javax.inject.Inject

class LayananFragment : BaseDaggerFragment() {

    @Inject
    lateinit var factory: ViewModelFactory
    val viewModel by lazy { ViewModelProviders.of(this,factory)[LayananFinansialViewModel::class.java] }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layanan_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addObserver()
        viewModel.getDetail()
    }

    private fun addObserver()  = viewModel.liveData.observe(this, Observer {
        it?.let {
            when(it){
                is Success -> render(it.data)
            }
        }
    })

    private fun render(data: LayananFinansialModel) {
        data.sectionList?.forEach{
            layanan_container.removeAllViews()
            val section = LayananSectionView(context)
            section.setData(it)
            layanan_container.addView(section)
        }
    }

    override fun getScreenName(): String = this.javaClass.name

    override fun initInjector() {
     getComponent(LayananComponent::class.java).inject(this)
    }
}