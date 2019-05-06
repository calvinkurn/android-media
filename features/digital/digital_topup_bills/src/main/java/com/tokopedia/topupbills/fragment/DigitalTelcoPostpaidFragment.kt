package com.tokopedia.topupbills.fragment

import android.support.v4.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment

/**
 * Created by nabillasabbaha on 06/05/19.
 */
class DigitalTelcoPascaFragment: BaseDaggerFragment() {

    override fun getScreenName(): String {
        return DigitalTelcoPascaFragment::class.java.simpleName
    }

    override fun initInjector() {

    }

    companion object {

        fun newInstance() : Fragment {
            val fragment = DigitalTelcoPascaFragment()
            return fragment
        }
    }
}
