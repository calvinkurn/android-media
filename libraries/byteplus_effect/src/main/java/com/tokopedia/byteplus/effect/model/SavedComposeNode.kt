package com.tokopedia.byteplus.effect.model

/**
 * Created By : Jonathan Darwin on March 29, 2023
 */
data class SavedComposeNode(
    val faceFilterComposeNodeApplied: Boolean,
    val faceFilters: List<FaceFilter>,
    val preset: Preset
) {
    fun clearFaceFilter(): SavedComposeNode = copy(faceFilterComposeNodeApplied = false, faceFilters = emptyList())

    fun clearPreset(): SavedComposeNode = copy(preset = Preset.Unknown)

    companion object {
        val Empty: SavedComposeNode
            get() = SavedComposeNode(
                faceFilterComposeNodeApplied = false,
                faceFilters = emptyList(),
                preset = Preset.Unknown
            )
    }
}
