---
title: "Feed"
labels:
  - feed
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->                                                                                                                                                                                                                                                                                                                                                                                                 |
| --- |---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Contributors** | [Shruti Agarwal (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/608fdbe3c87b550069a00200?ref=confluence), [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence), [Furqan](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence), [Fachrizal Achmad Zulfikar Mursalin](https://tokopedia.atlassian.net/wiki/people/62a6713a192edb006f9d9a2b?ref=confluence) |
| Product Manager | [Ayila Adzkiya Sucahyo](https://tokopedia.atlassian.net/wiki/people/62856e9d82e85d0068f45b40?ref=confluence), [Glorianna Chrysilla](https://tokopedia.atlassian.net/wiki/people/62cb5abf10fcc6f7ae3dc39a?ref=confluence)                                                                                                                                                                                                                          |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890)                                                                                                                                                                                                                                                                                                                                                  |
| Release date | 20 Jul 2021 / <!--start status:GREY-->MA-3.136<!--end status--> <!--start status:GREY-->SA-2.66<!--end status-->                                                                                                                                                                                                                                                                                                                                  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->                                                                                                                                                                                                                                                                                                                                                                                                |
| Product PRD | [New Feeds 2021+ Figma](/wiki/spaces/CN/pages/1205285904)                                                                                                                                                                                                                                                                                                                                                                                         |
| Module Location | `features.feed`                                                                                                                                                                                                                                                                                                                                                                                                                                   | `features/content/feed`  |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:20 July (MA-1.391/SA-2.91)-->
1. Feed Revamp -

<https://github.com/tokopedia/android-tokopedia-core/pull/20570>
<!--end expand-->

## Overview

Tokopedia Feed is the one stop on Tokopedia app for users to seamlessly browse and connect to shops as well as user generated content.
It is a centralised place for browsing content on Tokopedia just like we are scrolling through social media to see various types of posts containing media content, view product tags on those posts, like, share comment on these posts and further explore the shops and products visible.

### Project Description

Feed is the main content hub where users can view and create various types of media posts posted by sellers or users on Tokopedia platform.
Feed consists of several pages:
- Main Page (Update page)
- Content Detail Page
- Browse Page
- Category Inspirational Page

---

#### Main Page (Update Page)
After a lot of improvements, revamps and new features, our feed main page consists of the following main tabs:

- User Profile Entry Point - User profile entry point as a icon which redirects to the user profile page of logged in user.
- Update Tab - This page shows various typed of posts which are Seller Generated Content, Automated Content, TopAds posts,

Video Posts etc.
- Explore Tab - This page helps user explore and filter out posts based on various categories like gadgets, fashion etc. User can redirect from this page to Content detail Page for similar other posts.
- Video Tab - This page mainly helps users to explore through the video and play content, the live and upcoming video contents.
- `com.tokopedia.feedplus.view.fragment.FeedPlusContainerFragment` is the main parent fragment which displays the following child fragments in the three tabs:




	- Update Tab - 
	
	`com.tokopedia.feedplus.view.fragment.FeedPlusFragment`
	- Explore Tab - 
	
	`com.tokopedia.explore.view.fragment.ContentExploreFragment`
	- VideoTab - 
	
	`com.tokopedia.videoTabComponent.view.VideoTabFragment`

[Screen Recording 2023-03-13 at 12.44.12 PM.mov](/wiki/download/attachments/2109899439/Screen%20Recording%202023-03-13%20at%2012.44.12%20PM.mov?version=1&modificationDate=1678691772057&cacheVersion=1&api=v2&width=340)

---

#### Content Detail Page
- TBD

---

#### Browse Page ("tokopedia://feed/browse")
The purpose of this page is to showcase list of videos, authors to follow, and inspiration banners to inspire users and let them easily search for videos that might be to their liking.
This page is represented by `com.tokopedia.feedplus.browse.presentation.FeedBrowseFragment`.
It is constructed by a RecyclerView which has nested RecyclerView inside it. The current available widgets in this page are:
- Chips (to show menu for channel list, has nested RecyclerView for each chip menu)
- Channels (to show list of channels horizontally, has nested RecyclerView for each channel widget)
- Banner (to show Inspiration Banner, displayed in grid)
- Banner Placeholder (to show loading for Inspiration Banner, displayed in grid)
- Title (to show title for each slot of widgets)
- Authors (to show list of authors horizontally, has nested RecyclerView for each author widget)

---

#### Category Inspirational Page ("tokopedia://feed/browse/inspirational")
The purpose of this page is to show list of videos for a given inspiration category.
This page is represented by `com.tokopedia.feedplus.browse.presentation.CategoryInspirationFragment`.
it is constructed by a RecyclerView which has nested RecyclerView inside it (similar to Browse Page). The current available widgets in this page are:
- Chips (to show menu for channel list, has nested RecyclerView for each chip menu)
- Inspiration Card (to show video and the info of the author, displayed in grid)
- Inspiration Card Placeholder (to show loading for Inspiration Card, displayed in grid)
- Title (to show title for Inspiration Card slot)
- Loading (to show load more to indicate users that it is fetching data for next page)

## Tech Stack

Since feed is a very old module and it contains all the old classes,

elements and architecture which is being reused in the revamped version.

- **MVVM** (Model View ViewModel) architecture pattern is used in most of the fragments.
- **TypeFactory** is used like in most places.
- **Adapter Delegate** : In some new and revamped pages adapter like `PlaySeeMoreAdapter`.

We are using Adapter Delegate as replacement to Visitable Pattern. It’s part of our internal library and it’s already used DiffUtil under the hood. [Click here to see the official documentation.](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/697903385)

## Navigation

Here is the list of pages that are available in this module:



| **Status** | **Activity/Page Name** | **AppLink** | **Screen Recording** |
| --- | --- | --- | --- |
| <!--start status:GREEN-->CURRENTLY USED<!--end status--> | `com.tokopedia.feedplus.view.activity.FeedPlusDetailActivity` | Public → `tokopedia://feedcommunicationdetail/{post_id}``communicationdetail/{post_id}` | [Screen Recording 2023-02-01 at 10.43.05 AM.mov](/wiki/download/attachments/2109899439/Screen%20Recording%202023-02-01%20at%2010.43.05%20AM.mov?version=1&modificationDate=1675228505056&cacheVersion=1&api=v2)<br/> |
| <!--start status:GREEN-->CURRENTLY USED<!--end status--> | `com.tokopedia.feedplus.view.fragment.FeedPlusFragment` | Public → `tokopedia://feed` | [Screen Recording 2023-02-01 at 11.07.56 AM.mov](/wiki/download/attachments/2109899439/Screen%20Recording%202023-02-01%20at%2011.07.56%20AM.mov?version=1&modificationDate=1675229925098&cacheVersion=1&api=v2)<br/> |
| <!--start status:RED-->TO BE DELETED<!--end status--> | `com.tokopedia.feedplus.view.activity.PlayVideoLiveListActivity` | Public → `tokopedia://feedplaylivedetail?widgetType={live/upcoming}&source_type=abc&source_id=1&filter_category=Untukmu` | [Screen Recording 2023-02-01 at 12.00.47 PM.mov](/wiki/download/attachments/2109899439/Screen%20Recording%202023-02-01%20at%2012.00.47%20PM.mov?version=1&modificationDate=1675233354119&cacheVersion=1&api=v2)<br/>  |

## How To

To compile and run Feed on test app.

1. Switch to TestApp
2. Include modules `feature/content/feed` ,`feature/home/navigation` `feature/user/onboarding`
3. In MainActivity



```
fun goTo() {
    /* @example: open groupchat module;
      * startActivity(PlayActivity.getCallingIntent(this, "668", true))
      * or, you can use route like this:
      * RouteManager.route(this, ApplinkConstInternalMarketplace.SHOP_SETTINGS)
      * LEAVE THIS EMPTY AS DEFAULT!!
      * */
    val intent = Intent(this, MainParentActivity::class.java)
    startActivity(intent)
}
```

4. Include MainParentActivity in AndroidMainifest of test app.



---

## Action Items

We will soon be revamping our Feed Page completely to Unified Feed. To unify the access, we will introduce Feed as a hub to discover and browse all types of content, from live, short video, and image post content. Users will get to see their feed based on who they follow, recent shopping activity recommendations, live shopping deals from favorite shops. To strengthen more of this unification, we will further accommodate buyers to consume content in immersive view to engage with our content especially videos content in a less cluttered experience.

## Useful Links

### Unified Feed
- PRD for Unified Feed [PRD - Immersive Design Feed & Play](/wiki/spaces/CN/pages/2127563927)
- [PRD - Architecture Design Feed & Play](/wiki/spaces/CN/pages/2123967735)
- Figma For Unified Feed - <https://www.figma.com/file/EspwPfoYtYHWkqzF1yWiKf/Unified-Feed?node-id=1185%3A174317&t=BSJhNcGzDdAwvEPh-0>

### Browse Page
- [PRD for Browse Page](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/2286486513/PRD+-+Browse+page+in+Immersive+Feed)
- [Figma for Browse Page](https://www.figma.com/file/2ImVpbmsRCoiUjhzhQjeBA/%5BFeed%5D-Search---Mobile?type=design&node-id=8126-250440&mode=design&t=hwBHPOCiBhtNgNpw-4)
- [Tracker for Browse Page](https://mynakama.tokopedia.com/datatracker/requestdetail/view/4134)