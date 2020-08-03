package com.tokopedia.play.broadcaster.ui.model

/**
 * @author by furqan on 07/06/2020
 */
sealed class CoverSource {

    abstract val sourceString: String

    object None : CoverSource() {
        override val sourceString: String
            get() = NONE
    }
    object Camera : CoverSource() {
        override val sourceString: String
            get() = CAMERA
    }
    object Gallery : CoverSource() {
        override val sourceString: String
            get() = GALLERY
    }
    data class Product(val id: Long) : CoverSource() {
        override val sourceString: String
            get() = PRODUCT
    }

    companion object {

        private const val NONE = "none"
        private const val CAMERA = "camera"
        private const val GALLERY = "gallery"
        private const val PRODUCT = "product"

        fun getFromSourceString(sourceString: String, productId: Long? = null): CoverSource {
            return when (sourceString) {
                NONE -> None
                CAMERA -> Camera
                GALLERY -> Gallery
                PRODUCT -> Product(productId ?: throw IllegalStateException("Product id should not be null if source is from product"))
                else -> throw IllegalStateException("No known cover type for string: $sourceString")
            }
        }
    }
}