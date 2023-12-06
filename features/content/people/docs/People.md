---
title: "People"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Project Lead** | [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) / [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence)  |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence) / [Pandu Tyas Baghaskoro](https://tokopedia.atlassian.net/wiki/people/62d49480657fc166e25f259b?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date<br/> | 12 May 2022 / <!--start status:GREY-->MA-3.172<!--end status--> <!--start status:GREY-->SA-2.102<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [PRD - User Profile Onboarding Flow](/wiki/spaces/CN/pages/1852153655/PRD+-+User+Profile+Onboarding+Flow) [PRD - User Profile MVP Phase (Info Section + VOD/Livestream support)](/wiki/spaces/CN/pages/1838256788) [Shop Recommendation Widget](/wiki/spaces/CN/pages/1786419867/Shop+Recommendation+Widget)  |
| Module Location | `features.people` | `features/content/people` |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:19 Jan 2023 (MA 3.207/SA-2.137)-->
###### Fixing visibility issue in user profile regarding recommendation widget
PR: <https://github.com/tokopedia/android-tokopedia-core/pull/31307>
<!--end expand-->

<!--start expand:05 Jan 2023 (MA 3.204/SA-2.134)-->
###### User Profile - User cannot see 'Lihat Barang' button on Feed postPR: <https://github.com/tokopedia/android-tokopedia-core/pull/31023>
<!--end expand-->

<!--start expand:17 Nov 2022 (MA 3.202/SA-2.132)-->
###### User profile feed tab for feed post creationPR: <https://github.com/tokopedia/android-tokopedia-core/pull/30392>
<!--end expand-->

## Overview

**People** is a module name for User Profile feature under Feed. This module include all of the need for User Profile, but there is some other file/resources that we used from other module such as `content_common` & `feed_component`module.

### Background

Content tribe is planning to open content creation to non-sellers in Q1 2022 in order to increase content supply and variety within Feed and Play platform

. Naturally, these non-seller users will need a dedicated page to consolidate and showcase all of the content that they have created. User profiles will be developed to satisfy this need and more by allowing users to identify each other, find the content of a specific content creator, and follow them to see their future content in their Feed timelines.

### Project Description

People module or in the feature name called User Profile is a feature where user can see their profile information related to the content. Inside this User Profile, users can see their following and follower list, they can also upload feeds or videos and can view that in this User Profile. There is Shop Recommendations too which will appear when the user has no posts at all.  
  
Here where you can open this User Profile page inside our Tokopedia Main App.

- **Feed**

<img src="https://docs-android.tokopedia.net/images/docs/res/entry_point_1.png" alt="" />

Inside Feed, you can see there is user account profile picture in the very left of the Feed tab. You can directly open the User Profile Page from this.

- **Akun Saya**

<img src="https://docs-android.tokopedia.net/images/docs/res/entry_point_2.png" alt="" />

From settings, you can navigate to your edit profile information and find that Info Profile button, you can just click the icon and the bottom sheet will pop up right away.

<img src="https://docs-android.tokopedia.net/images/docs/res/entry_point_3.png" alt="" />

From the the bottom sheet, you can just click Lihat Halaman button and that will direct you to the User Profile page.

Other information from user profile you can see this images below.

<img src="https://docs-android.tokopedia.net/images/docs/res/user_profile1.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/user_profile3.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/user_profile4.png" alt="" />

Recommendation widget, account to follow.

<img src="https://docs-android.tokopedia.net/images/docs/res/device-2023-01-30-120852.png" alt="" />

There is also onboarding for user profile page.

<img src="https://docs-android.tokopedia.net/images/docs/res/device-2023-01-30-112052.png" alt="" />

User will get some banner information to create username, profile picture and biography for its user profile.

<img src="https://docs-android.tokopedia.net/images/docs/res/device-2023-01-30-112724.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/device-2023-01-30-113239.png" alt="" />

User who wants to creating some feed post will get this onboarding if they account has no username yet or never accept the TNC for creating post. This onboarding currently only for feed creation only; live stream & shorts will be in the next development.

## Tech Stack

There is several tech stack that we used in this User Profile page. 

- **Repository** : a new layer between ViewModel & UseCase. The advantages:


	- Easier to mock network since we only need to mock the repository instead of the usecases.
	- ViewModel constructor won’t be bloated by UseCases anymore.
	- As a place to translate DTO to UI Model.
- **MVI** : we haven’t implemented a full MVI yet, but we are trying to make it MVI. Any user events will be passed to ViewModel with only 1 entry point and the result will be emitted by only 1 UI State, so the pipeline flow is clear and easier to understand.
- **Flow** : previously we are using LiveData, but for new development we start using Flow (StateFlow & SharedFlow). Flow has a lot of useful operator than LiveData, and it’s suitable for our usecase where we combine all individual state to 1 UI state only. [Click here to see the official documentation.](https://developer.android.com/kotlin/flow)
- **Fragment Factory** : Most of the newer features already adopted Fragment Factory. With Fragment Factory, we are able to inject the dependency directly to Fragment constructor. [Click here to see the official documentation.](https://developer.android.com/reference/androidx/fragment/app/FragmentFactory)

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/Untitled%20Diagram.drawio.png" alt="" />

## Navigation

For the navigation to open User Profile is like these two screen record below.

[navigation\_from\_feed\_tab.webm](/wiki/download/attachments/2109735972/navigation_from_feed_tab.webm?version=1&modificationDate=1673949535333&cacheVersion=1&api=v2&width=340)

[navigation\_from\_edit\_profile.webm](/wiki/download/attachments/2109735972/navigation_from_edit_profile.webm?version=1&modificationDate=1673949551608&cacheVersion=1&api=v2&width=340)

[device-2023-01-30-115202.webm](/wiki/download/attachments/2109735972/device-2023-01-30-115202.webm?version=1&modificationDate=1675050885074&cacheVersion=1&api=v2&width=340)

## How-to

To open People module we need `userID` or `userName` as its parameter inside the `RouteManager.route()` and use `ApplinkConst.PROFILE` as the applink.



```
RouteManager.route(requireContext(), ApplinkConst.PROFILE, userID/userName)
```

## GQL List



| **GQL Name** | **Documentation** | **Description** |
| --- | --- | --- |
| `ProfileHeader` | <https://tokopedia.atlassian.net/l/cp/GKz0r40i>  | This GQL query is used to get some profile header informations. This informations is showed on profile page. |
| `ProfileIsFollowing` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1851075774?atlOrigin=eyJpIjoiNmIwYjUzZmNkNjViNDc5Yjk4MWIwYTY5NDRmYmU5OGIiLCJwIjoiYyJ9>  | This GQL query is used to check list of user ID is followed or not. This informations is showed on profile page.  |
| `FeedCheckWhitelist` | [[Feeds][GQL] FeedX GQL Header (feedXHeader)](/wiki/spaces/CN/pages/2157021022)  | This query should be able to show create button, list of tab, user profile, and live button. |
| `FeedXProfileTabs` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1936392295?atlOrigin=eyJpIjoiYWMxOTMzNGEzNWM0NDQ4NWIxMGJjYWM1MjAyN2NhOTciLCJwIjoiYyJ9>  | The query will return a list of existing tabs with their properties and metadata that tells which one is the default tab |
| `FeedXRecomWidget` | <https://tokopedia.atlassian.net/l/cp/88k8CHd0>  | This GQL query is used to show shop / user profile recommendation |
| `FeedXProfileGetProfilePosts` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2002062179?atlOrigin=eyJpIjoiNDE4ODVmNThkYTk3NDNjMDk2N2UzYTllYjQ1ZjQ5ZGYiLCJwIjoiYyJ9>  | The new GQL query `feedXProfileGetProfilePosts` should be capable to display feeds updates tab. All items/cards/UI from [Feed content type](https://docs.google.com/presentation/d/1k5teiC9G0_o4YeccxHQGOccehPFxXBKcCifvq5d-Z44/edit#slide=id.gafc06f2206_0_1) must be displayable by the new query. Changing from old feeds to this new query should result in no imperceptible changes in the updates tab. |
| `PlayGetContentSlot` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/798622188?atlOrigin=eyJpIjoiMjQ2ODJmNjcwMTEzNDUyOTkzYTIwN2NjMjUzM2UwYWQiLCJwIjoiYyJ9>  | Getting content slot layout and items. |
| `ProfileFollowerList` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1851075736?atlOrigin=eyJpIjoiMTZhMjMwOTgwMDRjNGJhN2E0OTIzMjFhZjkxMjE3ZWIiLCJwIjoiYyJ9>  | This GQL query is used to get list of user follower. This informations is showed on profile page. |
| `ProfileFollowingList` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1851075697?atlOrigin=eyJpIjoiYjEyNjFjMjEzM2Q0NGY2NWFhNDNhNGZiMjMzM2JlYTUiLCJwIjoiYyJ9>  | This GQL query is used to get list of user following. This informations is showed on profile page. Following list will handle shop and user following list. Following list will return in order from shop first then users sort by alphabet. |
| `SocialNetworkFollow` | <https://tokopedia.atlassian.net/wiki/spaces/AUT/pages/1879052249?atlOrigin=eyJpIjoiM2E2MGVlYWZkOTM5NDU4ZTk3NWFmYjg3ZjJkOThiY2YiLCJwIjoiYyJ9>  | This GQL query is used to do the follow user. |
| `SocialNetworkUnfollow` | <https://tokopedia.atlassian.net/wiki/spaces/AUT/pages/1879509430?atlOrigin=eyJpIjoiMTlhY2JjMjFiZWIxNDRhYzg5YmQ4YzVhNjI5YzQ3ODEiLCJwIjoiYyJ9>  | This GQL query is used to do the unfollow user. |
| `FollowShop` | <https://tokopedia.atlassian.net/wiki/spaces/MC/pages/709833521/Shop+Follow+Mutation?atlOrigin=eyJpIjoiZmM5NDAxMWNjOTliNDk4MGJjOTQyNDVjYzJmM2YyOWIiLCJwIjoiYyJ9>  | This QGL query is used to do follow or unfollow shop from recommendation widget. |
| `PlayToggleChannelReminder` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/820707853?atlOrigin=eyJpIjoiYTNkY2JkNDYyMzk1NDBkZjg3NmMxZjJjM2FhNDBlZjgiLCJwIjoiYyJ9>  | Set/unset reminder for given channel. Use insert, on conflict update query, which disallow duplication but will return success upon duplicate requests. |
| `FeedXProfileBlockUser` | <https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2131165426?atlOrigin=eyJpIjoiMjNmODliYjFmMjYzNDI3NjhiNjBmNzQzMmVjODRmMGUiLCJwIjoiYyJ9>  | The mutation will block targeted user, will return success |

## Action Items

- create instrument test
- onboarding for live stream
- onboarding for shorts

## Useful Links

- [Figma](https://www.figma.com/file/bzcgRUGyM5Z8QZhRkcOQD4/%5BFeed%5D-User-Feed-%26-Shop-Feed?node-id=1644%3A32740&t=kGj6t8nUMwH2iG4M-0)
- [Thanos](https://mynakama.tokopedia.com/datatracker/requestdetail/view/2443)

## FAQ

<!--start expand:Which testing account that we need to use for this user profile?-->
Basically, user profile will available at every Tokopedia account. You can reach out to [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890)
<!--end expand-->

<!--start expand:How to create feed post from user profile?-->
To create feed post from user profile, your account need to be whitelisted first and accept the T&C along with creating some username.
<!--end expand-->

<!--start expand:How to start live stream from user profile?-->
To create live stream from user profile, your account need to be whitelisted first and for now there is no onboarding in user profile page yet, instead you’ll get the onboarding inside the preparation page.
<!--end expand-->

