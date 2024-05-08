package com.tokopedia.people.model

import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel
import kotlin.random.Random

internal class FollowListModelBuilder {

    fun createFollowingData(
        followings: List<PeopleUiModel> = emptyList(),
        nextCursor: String = ""
    ) = FollowListUiModel.Following(
        total = FollowListUiModel.FollowCount("0", followings.size.toString()),
        followingList = followings,
        nextCursor = nextCursor
    )

    fun createFollowersData(
        followers: List<PeopleUiModel> = emptyList(),
        nextCursor: String = ""
    ) = FollowListUiModel.Follower(
        total = FollowListUiModel.FollowCount(followers.size.toString(), "0"),
        followers = followers,
        nextCursor = nextCursor
    )

    fun getFollowingData(
        count: Int = 5,
        nextCursor: String = ""
    ) = FollowListUiModel.Following(
        total = FollowListUiModel.FollowCount("0", count.toString()),
        followingList = createRandomPeopleList(count),
        nextCursor = nextCursor
    )

    fun getFollowersData(
        count: Int = 5,
        nextCursor: String = ""
    ) = FollowListUiModel.Follower(
        total = FollowListUiModel.FollowCount(count.toString(), "0"),
        followers = createRandomPeopleList(count),
        nextCursor = nextCursor
    )

    fun createRandomPeopleList(
        count: Int = 5
    ): List<PeopleUiModel> {
        return buildList {
            while (lastIndex != count) {
                val randomPeople = createRandomPeople()
                if (!contains(randomPeople)) add(randomPeople)
            }
        }
    }

    fun createRandomPeople(): PeopleUiModel {
        val generatedInt = Random.nextInt()
        return if (generatedInt.isEven()) {
            createRandomUser()
        } else {
            createRandomShop()
        }
    }

    fun createRandomUser(): PeopleUiModel.UserUiModel {
        val randomId = Random.nextInt().toString()
        return PeopleUiModel.UserUiModel(
            id = randomId,
            encryptedId = randomId,
            photoUrl = "",
            name = "User $randomId",
            username = "user_$randomId",
            isFollowed = false,
            isMySelf = false,
            appLink = ""
        )
    }

    fun createRandomShop(): PeopleUiModel.ShopUiModel {
        val randomId = Random.nextInt().toString()
        return PeopleUiModel.ShopUiModel(
            id = randomId,
            name = "Shop $randomId",
            isFollowed = false,
            appLink = "",
            logoUrl = "",
            badgeUrl = ""
        )
    }

    fun createUser(
        id: String = Random.nextInt().toString(),
        isFollowed: Boolean = false,
        isMySelf: Boolean = false
    ) = PeopleUiModel.UserUiModel(
        id = id,
        encryptedId = id,
        photoUrl = "",
        name = "User $id",
        username = "user_$id",
        isFollowed = isFollowed,
        isMySelf = isMySelf,
        appLink = ""
    )

    fun createShop(
        id: String = Random.nextInt().toString(),
        isFollowed: Boolean = false
    ) = PeopleUiModel.ShopUiModel(
        id = id,
        name = "Shop $id",
        isFollowed = isFollowed,
        appLink = "",
        logoUrl = "",
        badgeUrl = ""
    )
}
