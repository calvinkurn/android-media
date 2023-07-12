package com.tokopedia.tokochat_common.view.chatlist

import androidx.viewbinding.ViewBinding
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.tokochat_common.databinding.TokochatListBaseFragmentBinding
import com.tokopedia.utils.lifecycle.autoClearedNullable

abstract class TokoChatListBaseFragment<viewBinding : ViewBinding> : BaseDaggerFragment() {

    protected var binding: viewBinding? by autoClearedNullable()
    protected var baseBinding: TokochatListBaseFragmentBinding? by autoClearedNullable()


}
