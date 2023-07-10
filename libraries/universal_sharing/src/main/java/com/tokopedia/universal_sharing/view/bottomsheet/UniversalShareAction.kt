package com.tokopedia.universal_sharing.view.bottomsheet

sealed class UniversalShareAction

data class RenderChips(val data: List<Map<Int, String>>) : UniversalShareAction()
data class ChipSelected(val id: Int) : UniversalShareAction()
