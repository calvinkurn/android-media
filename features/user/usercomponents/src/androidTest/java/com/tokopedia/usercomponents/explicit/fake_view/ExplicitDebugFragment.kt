package com.tokopedia.usercomponents.explicit.fake_view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.usercomponents.explicit.cassava.ExplicitCassava
import com.tokopedia.usercomponents.explicit.di.FakeExplicitComponent
import com.tokopedia.usercomponents.explicit.view.ExplicitData
import com.tokopedia.usercomponents.explicit.view.ExplicitView
import com.tokopedia.usercomponents.explicit.view.viewmodel.ExplicitViewContract
import com.tokopedia.usercomponents.test.R
import javax.inject.Inject

class ExplicitDebugFragment : BaseDaggerFragment() {


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
        view.findViewById<ExplicitView>(R.id.fake_explicit)?.setupView(
            explicitViewContract,
            ExplicitData(
                ExplicitCassava.VALUE_TEMPLATE_NAME,
                ExplicitCassava.VALUE_PAGE_NAME,
                ExplicitCassava.VALUE_PAGE_PATH,
                ExplicitCassava.VALUE_PAGE_TYPE
            )
        )
    }

    override fun getScreenName(): String =
        ExplicitDebugActivity::class.java.simpleName

    override fun initInjector() {
        component?.inject(this)
    }

    companion object {
        var component: FakeExplicitComponent? = null
    }
}
