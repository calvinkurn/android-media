package com.tokopedia.effect.model

/**
 * Created By : Jonathan Darwin on March 29, 2023
 */
data class SavedComposeNode(
    val faceFilterComposeNode: String,
    val faceFilters: List<FaceFilter>,
    val preset: Preset
) {
    val faceFilterComposeNodeApplied: Boolean
        get() = faceFilterComposeNode.isNotEmpty()

    val presetComposeNodeApplied: Boolean
        get() = preset.key.isNotEmpty()

    fun clearFaceFilter(): SavedComposeNode = copy(faceFilterComposeNode = "", faceFilters = emptyList())

    fun clearPreset(): SavedComposeNode = copy(preset = Preset.Unknown)

    companion object {
        val Empty: SavedComposeNode
            get() = SavedComposeNode(
                faceFilterComposeNode = "",
                faceFilters = emptyList(),
                preset = Preset.Unknown
            )
    }
}
