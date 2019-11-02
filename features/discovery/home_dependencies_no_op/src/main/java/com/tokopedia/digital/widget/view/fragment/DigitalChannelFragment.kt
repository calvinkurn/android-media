package com.tokopedia.digital.widget.view.fragment

import android.support.v4.app.Fragment
import com.tokopedia.digital.widget.view.listener.DigitalChannelFragmentInteraction

class DigitalChannelFragment: Fragment() {

    companion object {

        @JvmStatic
        fun newInstance(interaction: DigitalChannelFragmentInteraction): Fragment {
            return DigitalChannelFragment()
        }
    }
}