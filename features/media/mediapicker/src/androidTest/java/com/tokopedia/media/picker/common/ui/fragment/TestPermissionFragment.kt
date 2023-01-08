package com.tokopedia.media.picker.common.ui.fragment

import androidx.lifecycle.ViewModelProvider
import com.tokopedia.media.picker.ui.fragment.permission.PermissionFragment
import javax.inject.Inject

class TestPermissionFragment @Inject constructor(
    viewModelFactory: ViewModelProvider.Factory
) : PermissionFragment(viewModelFactory)