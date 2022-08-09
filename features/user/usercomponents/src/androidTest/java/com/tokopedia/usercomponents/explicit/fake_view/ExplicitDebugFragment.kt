package com.tokopedia.usercomponents.explicit.fake_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.usercomponents.explicit.ExplicitTest
import com.tokopedia.usercomponents.explicit.di.DaggerFakeExplicitComponent
import com.tokopedia.usercomponents.explicit.view.ExplicitData
import com.tokopedia.usercomponents.explicit.view.interactor.ExplicitViewContract
import com.tokopedia.usercomponents.test.R
import com.tokopedia.usercomponents.test.databinding.FragmentExplicitDebugBinding
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class ExplicitDebugFragment : BaseDaggerFragment() {

    private val binding: FragmentExplicitDebugBinding? by viewBinding()

    @Inject
    lateinit var explicitViewContract: ExplicitViewContract

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_explicit_debug, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.fakeExplicit?.setupView(
            explicitViewContract,
            ExplicitData(
                "halal_single",
                "halal_single_name",
                "halal_single_path",
                "halal_single_type"
            )
        )
    }

    override fun getScreenName(): String =
        ExplicitDebugActivity::class.java.simpleName

    override fun initInjector() {
        ExplicitTest.component?.inject(this)
    }
}