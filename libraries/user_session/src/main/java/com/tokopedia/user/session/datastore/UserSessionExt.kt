package com.tokopedia.user.session.datastore

import com.tokopedia.user.session.UserSessionProto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

fun UserSessionProto.toUserData(): UserData {
    return UserData(
	name = name,
	email = email,
	phoneNumber = phoneNumber,
	accessToken = accessToken,
	refreshToken = refreshToken
    )
}

//fun Flow<UserSessionProto>.toUserDataFlow(): Flow<UserData> {
//    return runBlocking {
//	this@toUserDataFlow.map { proto ->
//            UserData(
//                name = proto.name,
//                email = proto.email,
//                phoneNumber = proto.phoneNumber,
//                accessToken = proto.accessToken,
//                refreshToken = proto.refreshToken
//            )
//        }
//    }
//}

fun Flow<UserSessionProto>.toBlocking(): UserData {
    return runBlocking {
	this@toBlocking.first().toUserData()
    }
}


fun Flow<String>.toBlocking(): String {
    return runBlocking {
	this@toBlocking.first()
    }
}

suspend fun Flow<String>.value(): String {
    return this.first()
}

fun Flow<Boolean>.toBlocking(): Boolean {
    return runBlocking {
	this@toBlocking.first()
    }
}