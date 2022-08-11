package com.tokopedia.encryption.utils

object Constants {
    const val RSA_METHOD = "RSA"
    const val AES_METHOD = "AES"
    const val RSA_LENGTH = 2048
    const val RSA_PKCS1_ALGORITHM = "RSA/ECB/PKCS1Padding"
    const val RSA_OAEP_ALGORITHM = "RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"
    const val ARC4_ALGORITHM = "ARCFOUR"
    const val BLOWFISH_ALGORITHM = "Blowfish"
    const val DES_ALGORITHM = "DES"
    const val DES_KEY_SIZE = 56
    const val DESede_METHOD = "DESede"
    const val DESede_ALGORITHM = "DESede"
    const val DESede_KEY_SIZE = 112
    const val ECB_ALGORITHM = "AES/ECB/PKCS5Padding"
    const val CBC_ALGORITHM = "AES/CBC/PKCS5Padding"
    const val GCM_256_ALGORITHM = "AES_256/GCM/NoPadding"
}
