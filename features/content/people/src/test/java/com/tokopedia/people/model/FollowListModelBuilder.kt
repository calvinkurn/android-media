package com.tokopedia.people.model

import com.tokopedia.kotlin.extensions.view.isEven
import com.tokopedia.people.views.uimodel.FollowListUiModel
import com.tokopedia.people.views.uimodel.PeopleUiModel
import kotlin.random.Random

internal class FollowListModelBuilder {

    fun getFollowingData(
        count: Int = 5
    ) = FollowListUiModel.Following(
        total = FollowListUiModel.FollowCount("0", count.toString()),
        followingList = createRandomPeopleList(count),
        nextCursor = ""
    )

    fun getFollowersData(
        count: Int = 5
    ) = FollowListUiModel.Follower(
        total = FollowListUiModel.FollowCount(count.toString(), "0"),
        followers = createRandomPeopleList(count),
        nextCursor = ""
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
}
