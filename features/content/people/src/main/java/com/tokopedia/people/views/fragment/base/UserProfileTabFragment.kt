package com.tokopedia.people.views.fragment.base

import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.people.views.fragment.contract.UserProfileFragmentContract

/**
 * Created by Jonathan Darwin on 08 March 2024
 */
abstract class UserProfileTabFragment : TkpdBaseV4Fragment() {

    fun isParentFragmentConfigChanges(): Boolean {
        return (parentFragment as? UserProfileFragmentContract)?.isParentConfigChanges ?: false
    }
}
