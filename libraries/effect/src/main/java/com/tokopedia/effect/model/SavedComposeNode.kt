package com.tokopedia.effect.model

/**
 * Created By : Jonathan Darwin on March 29, 2023
 */
data class SavedComposeNode(
    val faceFilterComposeNodeApplied: Boolean,
    val faceFilters: List<FaceFilter>,
    val presetComposeNodeApplied: Boolean,
    val preset: Preset
) {
    fun clearFaceFilter(): SavedComposeNode = copy(faceFilterComposeNodeApplied = false, faceFilters = emptyList())

    fun clearPreset(): SavedComposeNode = copy(presetComposeNodeApplied = false, preset = Preset.Unknown)

    companion object {
        val Empty: SavedComposeNode
            get() = SavedComposeNode(
                faceFilterComposeNodeApplied = false,
                faceFilters = emptyList(),
                presetComposeNodeApplied = false,
                preset = Preset.Unknown
            )
    }
}
