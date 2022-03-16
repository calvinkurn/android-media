package com.tokopedia.people.views

import com.tokopedia.people.UserProfileAnalytics

class UserProfileTracker {

    fun openUserProfile(isLoggedIn: Boolean, screenName:String, sessionIris: String, userId: String, live: Boolean){
        val label = if(live){
            "live"
        } else{
            "not live"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        map[UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS] = isLoggedIn
        map[UserProfileAnalytics.Constants.SCREEN_NAME] = "$screenName - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickBack(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_BACK
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickShare(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_SHARE
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickBurgerMenu(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_BURGER_MENU
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickProfilePicture(sessionIris: String, userId: String, self: Boolean, activityId: String){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_PROFILE_PICTURE
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$activityId - $userId - $label - live"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickFollowers(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FOLLOWER
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickFollowing(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FOLLOWING
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickSelengkapnya(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_SELENGKAPNYA
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickFollow(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FOLLOW
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickUnfollow(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_UNFOLLOW
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun clickVideoTab(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_VIDEO_TAB
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE

        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)

    }

    fun impressionVideo(sessionIris: String, userId: String, self: Boolean, live: Boolean, activityId: String, imageUrl: String, videoPosition: Int){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val labelLive = if(live){
            "live"
        } else{
            "vod"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_ITEM
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_VIDEO
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$activityId - $userId - $label - $labelLive"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        val promoMap = mutableMapOf<String, Any>()
        promoMap["creative_name"] = imageUrl
        promoMap["creative_slot"] = videoPosition
        promoMap["item_id"] = activityId
        promoMap["item_name"] = "feed user profile - video"
        map[UserProfileAnalytics.Constants.PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickVideo(sessionIris: String, userId: String, self: Boolean, live: Boolean, activityId: String, imageUrl: String, videoPosition: Int){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val labelLive = if(live){
            "live"
        } else{
            "vod"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_SELECT_CONTENT
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_VIDEO
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$activityId - $userId - $label - $labelLive"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        val promoMap = mutableMapOf<String, Any>()
        promoMap["creative_name"] = imageUrl
        promoMap["creative_slot"] = videoPosition
        promoMap["item_id"] = activityId
        promoMap["item_name"] = "feed user profile - video"
        map[UserProfileAnalytics.Constants.PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickFeedTab(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FEED_TAB
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun impressionPost(sessionIris: String, userId: String, self: Boolean, activityId: String, imageUrl: String, postPosition: Int, mediaType: String){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_ITEM
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_POST
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$activityId - $userId - $label - $mediaType"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        val promoMap = mutableMapOf<String, Any>()
        promoMap["creative_name"] = imageUrl
        promoMap["creative_slot"] = postPosition
        promoMap["item_id"] = activityId
        promoMap["item_name"] = "feed user profile - post"
        map[UserProfileAnalytics.Constants.PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickPost(sessionIris: String, userId: String, self: Boolean, activityId: String, imageUrl: String, postPosition: Int, mediaType: String){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_SELECT_CONTENT
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_POST
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$activityId - $userId - $label - $mediaType"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        val promoMap = mutableMapOf<String, Any>()
        promoMap["creative_name"] = imageUrl
        promoMap["creative_slot"] = postPosition
        promoMap["item_id"] = activityId
        promoMap["item_name"] = "feed user profile - post"
        map[UserProfileAnalytics.Constants.PROMOTIONS] = listOf(promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun openFollowersTab(isLoggedIn: Boolean, sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
        map[UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS] = isLoggedIn
        map[UserProfileAnalytics.Constants.SCREEN_NAME] = "feed user profile - follower tab"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickUserFollowers(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_USER
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickFollowFromFollowers(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FOLLOW
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickUnfollowFromFollowers(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_UNFOLLOW
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWER_TAB
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun openFollowingTab(isLoggedIn: Boolean, sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_OPEN_SCREEN
        map[UserProfileAnalytics.Constants.IS_LOGGED_IN_STATUS] = isLoggedIn
        map[UserProfileAnalytics.Constants.SCREEN_NAME] = "feed user profile - following tab"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickUserFollowing(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_USER
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickFollowFromFollowing(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FOLLOW
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickUnfollowFromFollowing(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_FEED
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_UNFOLLOW
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_FOLLOWING_TAB
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun impressionProfileCompletionPrompt(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_PROFILE_COMPLETION_PROMPT
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickProfileCompletionPrompt(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_PROFILE_COMPLETION_PROMPT
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun impressionProfileRecommendation(sessionIris: String, userId: String, shopId: String, imageUrl: String, postPosition: Int){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_ITEM
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_PROFILE_RECOMMENDATION
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $shopId"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        val promoMap = mutableMapOf<String, Any>()
        promoMap["creative_name"] = imageUrl
        promoMap["creative_slot"] = postPosition
        promoMap["item_id"] = shopId
        promoMap["item_name"] = "feed user profile - profile recommendations carousel"
        map[UserProfileAnalytics.Constants.PROMOTIONS] = listOf(promoMap, promoMap)
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickProfileRecommendation(sessionIris: String, userId: String, shopId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_SELECT_CONTENT
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_PROFILE_RECOMMENDATION
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $shopId"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickFollowProfileRecommendation(sessionIris: String, userId: String, shopId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_FOLLOW_PROFILE_RECOMMENDATION
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $shopId"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickCreatePost(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_CREATE_POST
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun impressionOnBoardingBottomSheetWithUsername(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickLanjutOnBoardingBottomSheetWithUsername(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITH_USERNAME
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun impressionOnBoardingBottomSheetWithoutUsername(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickLanjutOnBoardingBottomSheetWithoutUsername(sessionIris: String, userId: String){
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_HOME_PAGE
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.IMPRESSION_ONBOARDING_BOTTOMSHEET_WITHOUT_USERNAME
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE_ONBOARDING_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = userId
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickShareButton(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_SHARE_BUTTON
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickCloseShareButton(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_CLOSE_SHARE_BUTTON
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickShareChannel(sessionIris: String, userId: String, self: Boolean, channel: String){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_SHARE_CHANNEL
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$channel - $userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun viewShareChannel(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.VIEW_SHARE_CHANNEL
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun viewScreenshotShareBottomsheet(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_VIEW_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.VIEW_SHARE_SCREENSHOT_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickCloseScreenshotShareBottomsheet(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_CLOSE_SHARE_SCREENSHOT_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickChannelScreenshotShareBottomsheet(sessionIris: String, userId: String, self: Boolean){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_CHANNEL_SHARE_SCREENSHOT_BOTTOMSHEET
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }

    fun clickAccessMedia(sessionIris: String, userId: String, self: Boolean, allow:String){
        val label = if(self){
            "self"
        } else{
            "visitor"
        }
        val map = mutableMapOf<String, Any>()
        map[UserProfileAnalytics.Constants.EVENT] = UserProfileAnalytics.Event.EVENT_CLICK_COMMUNICATION
        map[UserProfileAnalytics.Constants.EVENT_ACTION] = UserProfileAnalytics.Action.CLICK_ACCESS_MEDIA
        map[UserProfileAnalytics.Constants.EVENT_CATEGORY] = UserProfileAnalytics.Category.FEED_USER_PROFILE
        map[UserProfileAnalytics.Constants.EVENT_LABEL] = "$allow - $userId - $label"
        map[UserProfileAnalytics.Constants.SESSION_IRIS] = sessionIris
        map[UserProfileAnalytics.Constants.USER_ID] = userId
        map[UserProfileAnalytics.Constants.BUSINESS_UNIT] = UserProfileAnalytics.Constants.CONTENT
        map[UserProfileAnalytics.Constants.CURRENT_SITE] = UserProfileAnalytics.Constants.TOKOPEDIA_MARKETPLACE
        UserProfileAnalytics().analyticTracker.sendGeneralEvent(map)
    }
}