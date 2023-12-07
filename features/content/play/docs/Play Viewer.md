---
title: "Play Viewer"
labels:
- play
---






| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->  |
| --- | --- |
| **Project Lead** | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Kenny Rizky Hadisaputra](https://tokopedia.atlassian.net/wiki/people/5d1471f0b8c82e0c0ff12c67?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Asti Dhiya Anzaria](https://tokopedia.atlassian.net/wiki/people/5ff2a8373b5e470138d51a05?ref=confluence)  |
| Product Manager | [Calvin Alfares Lamda Saputra](https://tokopedia.atlassian.net/wiki/people/60bdaf042bd21400692423d7?ref=confluence) [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 09 Aug 2022 / <!--start status:GREEN-->MA-3.61<!--end status--> <!--start status:GREY-->SA-2.131<!--end status-->  |
| Module type | <!--start status:YELLOW-->FEATURE<!--end status-->  |
| Product PRD | [Live Room Experience](/wiki/spaces/CN/pages/677840618/Live+Room+Experience) |
| Module Location | `features.play` | `features/content/play` |
| AppLink & WebLink | - tokopedia://play/{channel\_id}<br/>- tokopedia://play/{channel\_id}<br/>?source\_type={source\_type}<br/>&source\_id={source\_id}<br/>- tokopedia://play/channel\_recom<br/>- <https://www.tokopedia.com/play/channel/>{channel\_id}<br/> |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:16 Dec (MA-3.202/SA-2.132)-->
###### *Details*

- One Click Checkout 

 <https://github.com/tokopedia/android-tokopedia-core/pull/30470>
- Fix auto swiping issues <https://github.com/tokopedia/android-tokopedia-core/pull/30314>
<!--end expand-->

<!--start expand:9 Dec (MA-3.201/SA-2.131)-->
###### *Details*

Voucher Widget <https://github.com/tokopedia/android-tokopedia-core/pull/29962>
<!--end expand-->

<!--start expand:18 Nov (MA-3.200/SA-2.130)-->
###### *Details*

Product card impression adjustment <https://github.com/tokopedia/android-tokopedia-core/pull/30156>
<!--end expand-->

<!--start expand:4 Nov (MA-3.198/SA-2.128)-->
###### *Details*

Swiping coachmark <https://github.com/tokopedia/android-tokopedia-core/pull/29628>
<!--end expand-->

<!--start expand:27 Oct (MA-3.197/SA-2.127)-->
###### *Details*

Remove quiz prize <https://github.com/tokopedia/android-tokopedia-core/pull/29382>
<!--end expand-->

## Overview

Play's live room has experienced major changes focusing to cater commerce content. Phase 1 development has been in placed to support the launch of commerce content. To support a seamless live shopping experience, we are planning add new features for user inside live room. From content browsing to shopping experience. 

<img src="https://docs-android.tokopedia.net/images/docs/res/ezgif.com-video-to-gif.gif" alt="" />

### Background

To make users discover, watch, and engage in the PLAY contents, as well as maintain delightful live streaming experience to retain and make users convert.

### Project Description

Play Viewer is a room where user can watch VOD (video-on-demand), live video, Youtube video and upcoming live video as well, you can see pictures below (**Channel Type)** for reference. It allows user to interact with seller or KOL as well thru chat (in live video), like the video, comment on VOD (coming soon) and play games like giveaway and quiz.

We have integrated our feature with Tokopedia NOW to increase user’s awareness, NOW wants to produce engaging content through PLAY to attract new buyers and give our buyers more inspiration for groceries and household items.

To make user engage there is several feature that Play has,



1. Games, seller can create a game (give away and quiz) and reward the winner.
2. Product bottom sheet and carousel, to browse product easier we have release the pinned product feature. User can see the pinned product at the very top or left (no need to browse!) what product that seller currently talking about.
3. Promo bottom sheet, promo widget. To make promo more visible we have release promo widget which highlighted selected promo that the seller currently held. When user clicked, it opens promo bottom sheet in instant.
4. Explore widget, user can browse other channel to find contents that they want to see.
5. Chat: user can interact with streamer. User can ask more about product or anything.
6. RTN (real-time notification): show information about user (viewers) like welcoming message, or another user just followed the streamer, or they’re buying /eye-ing specific product
7. Pinned message: streamer can pinned a message in live room.
8. Like: user can like a video.
9. PiP (picture-in-picture): Multi-window mode user can browse Tokopedia apps or other apps while watching video in Play Viewer.
10. <!--start status:RED-->CURRENTLY DISABLED<!--end status--> Chromecast: user can cast the video they’re currently watch to their casting device / smart TV.
11. Share: user can share room link to invite others to watch the content.
12. Watch mode: user can access immersive/full-screen mode by tapping screen or access it via three-dots menu.
13. Report video: if user feel something’s off with video they’re currently watching they can report it by accessing three dots menu, select the reason and filled in detail.
14. <!--start status:YELLOW-->COMING SOON<!--end status--> Comment on VOD: user can leave and read comments in VOD room.
15. Pinned product: product that streamer pinned, it might be the product they’re talking about at the moment.
16. Reminder in upcoming page: user can turn reminder on if they don’t want to missed the video streaming.

### App Link

- `tokopedia://play/{channelId}`
- `tokopedia://play/channel_recomm` (Available since <!--start status:BLUE-->MA-3.208<!--end status--> )

### Channel Type







**VOD** (video controller is visible)

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-02-07%20at%2017.56.32.png" alt="" />





**LIVE** (chat box is visible)

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-02-07%20at%2017.45.29.png" alt="" />





**Youtube**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-02-07%20at%2017.55.41.png" alt="" />







**Archived**

<img src="https://docs-android.tokopedia.net/images/docs/res/421f8682-6fcf-49b6-926b-3240d5bcf938%23media-blob-url%3Dtrue%26id%3D0ae7c5d3-02fd-4441-8575-6a9920a430bc%26contextId%3D1439608%26collection%3D" alt="" />





**Deleted**

<img src="https://docs-android.tokopedia.net/images/docs/res/421f8682-6fcf-49b6-926b-3240d5bcf938%23media-blob-url%3Dtrue%26id%3D0ae7c5d3-02fd-4441-8575-6a9920a430bc%26contextId%3D1439608%26collection%3D" alt="" />





**Upcoming**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-02-27%20at%2012.13.25.png" alt="" />







### Orientation







**Vertical**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-02-07%20at%2017.45.29.png" alt="" />





**Horizontal**

<img src="https://docs-android.tokopedia.net/images/docs/res/Screen%20Shot%202023-02-07%20at%2017.51.10.png" alt="" />







## Tech Stack

- <https://exoplayer.dev/>: Exo Player supports features that not currently supported by Android’s MediaPlayer API.
- [Web Socket](https://www.geeksforgeeks.org/what-is-web-socket-and-how-it-is-different-from-the-http/) 


, using OkHttp: WebSocket makes event-driven programming easier, it allows the client or a server to send the message. Used for a real-time update for likes, game’s winner, etc.
- [SSE](https://www.w3schools.com/html/html5_serversentevents.asp), in upcoming page: Enabling a client to receive automatic updates from a server via an HTTP connection (automatic updates for channel status, is already live?).
- [Flow](https://developer.android.com/kotlin/flow): Mixed with Live Data. But we are migrating to Flow gradually.
- [Coroutine](https://developer.android.com/kotlin/coroutines): To simplify code that executes asynchronously.
- [MVVM](https://developer.android.com/topic/architecture): Google recommends. MVVM architecture is a Model-View-ViewModel architecture.
- [Floating Window](/wiki/spaces/PA/pages/2131364162/Floating+Window): an internal library that allows developers to implement a view that can draw over other apps in a simple way.

<img src="https://docs-android.tokopedia.net/images/docs/res/play-tech-stack3.png" alt="" />

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/play_viewer_diagram.drawio.png" alt="" />

## Navigation

Since version <!--start status:PURPLE-->MA-3.209<!--end status--> user can enter live room from dynamic icon in Home. 

<img src="https://docs-android.tokopedia.net/images/docs/res/ezgif.com-video-to-gif%20%284%29.gif" alt="" />

User can also enter the live room via these entry point (from Play Widget)

- Product Detail Page (PDP)
- Home
- Video Tab (Feed)
- Disco Page
- Explore Widget

<img src="https://docs-android.tokopedia.net/images/docs/res/ezgif.com-video-to-gif%20%283%29.gif" alt="" />



---

## How
 to



```
//need to pass channel ID
RouteManager.route(context, ApplinkConst.PLAY_DETAIL, channelId) 

//will generate recommended channels
RouteManager.route(context, ApplinkConst.PLAY_RECOM)
```

## GQL List



| **Name** | **Tech Documentation** | **Usage** |
| --- | --- | --- |
| `playGetChannelDetailsWithRecom` | [Get Channel Details with Recom (GQL)](/wiki/spaces/CN/pages/1153176257)  | Get channel details |
| `feedGetIsLikePost` | [Get isLike](/wiki/spaces/CN/pages/611977984/Get+isLike)  | Get status whether the user is already liked the content or no. |
| `playGetTagsItemSection` | [Get Channel Tags Item Section (GQL)](/wiki/spaces/CN/pages/1892014438)  | Get channel products and vouchers. |
| `playGetChannelsStatus` | [Play Get Channel Status (GQL)](/wiki/spaces/CN/pages/1166157984)  | Get channel status. |
| `playGetSocketCredential` | [Get Socket Credential - GC Token (GQL)](/wiki/spaces/CN/pages/851642028)  | Get socket credential to connect to web socket. |
| `playInteractiveGetChatHistory` | [Get Chat History](/wiki/spaces/CN/pages/2063827736)  | Get chat history in channel. |
| `playInteractiveGetCurrentInteractive` | [Play Interactive Get Current Interactive (GQL)](/wiki/spaces/CN/pages/1545055033)  | Get current game that currently playing (if any). It only called if remote config is on and channel is <!--start status:GREEN-->LIVE<!--end status--> and vertical. |
| `playInteractiveViewerGetLeaderboardWithSlot` | [PlayInteractiveViewerGetLeaderboardWithSlot](/wiki/spaces/CN/pages/1919516695)  | Get game leaderboard, if channel ever created game. |
| `playAnswerQuiz` | [GQL Mutation - PlayInteractiveAnswerQuiz](/wiki/spaces/CN/pages/1917624178)  | <!--start status:GREEN-->LIVE<!--end status--> Send user’s answer if they’re playing game. |
| `playInteractiveUserTapSession` | [Play Interactive User Tap Session (GQL)](/wiki/spaces/CN/pages/1545308650)  | <!--start status:GREEN-->LIVE<!--end status--> Send tap-tap when user is playing giveaway (game). |
| `do_like_kol_post` | [[GQL][Mutation] do\_like\_kol\_post](/wiki/spaces/CN/pages/2157021686)  | Send like if user clicked like. |
| `shopInfoByID` | [Shop Info GQL](/wiki/spaces/MC/pages/578519558/Shop+Info+GQL)  | Get seller info (if channel is created from seller) such as, name, id and follow status (whether user already followed or no) |
| `feedXProfileIsFollowing` | [[GQL] Profile Is Following](/wiki/spaces/CN/pages/1851075774)  | Get streamer info (if channel is created from KOL) such as, name, id, encrypted id (for send follow and unfollow) and follow status (whether user already followed or no) |
| `followShop` | [Shop Follow Mutation](/wiki/spaces/MC/pages/709833521/Shop+Follow+Mutation)  | To follow/unfollow seller. |
| `SocialNetworkFollow` | [[GQL] Follow](/wiki/spaces/AUT/pages/1879052249)  | To follow KOL. |
| `SocialNetworkUnfollow` | [[GQL] Unfollow](/wiki/spaces/AUT/pages/1879509430)  | To unfollow KOL. |
| `getProductVariant` | [[Query] Get PDP Variant Info](/wiki/spaces/MC/pages/523110738)  | To get product variant. |
| `add_to_cart_v2` | [[GraphQL] Add to Cart V2](/wiki/spaces/TTD/pages/1286768459)  | Add to cart product. |
| `add_to_cart_occ_multi` | [[GraphQL] Add To Cart OCC Multi](/wiki/spaces/TTD/pages/1602814814)  | Add to cart product from one-click-checkout / Beli Langsung. |
| `getCampaignNotifyMe` | [GQL -Get Campaign Notify Me](/wiki/spaces/CAM/pages/837256631/GQL+-Get+Campaign+Notify+Me)  | <!--start status:YELLOW-->RILISAN SPESIAL<!--end status--> <!--start status:BLUE-->UPCOMING<!--end status--> Get reminder bell status in upcoming section, whether is already reminded from user or no. |
| `checkCampaignNotifyMe` | [GQL - Check Campaign Notify Me](/wiki/spaces/CAM/pages/714605935/GQL+-+Check+Campaign+Notify+Me)  | <!--start status:YELLOW-->RILISAN SPESIAL<!--end status--><!--start status:BLUE-->UPCOMING<!--end status--> Send reminder / remove reminder for upcoming section (if user clicked bell) in product list. |
| `broadcasterReportTrackViewer` | [Broadcaster Report GQL](/wiki/spaces/CN/pages/894702622)  | Send product(s) tracker to broadcaster. |
| `broadcasterReportVisitChannel` | [Broadcaster Report GQL](/wiki/spaces/CN/pages/894702622)  | Send visit channel tracker to broadcaster. |
| `visionGetReportVideoReason` | [Report Video Reason](/wiki/spaces/RK/pages/1855033898/Report+Video+Reason)  | Get report reasoning list. Called when user open Report page from three-dots menu. |
| `visionPostReportVideoPlay` | [User Report Play Video Submission](/wiki/spaces/RK/pages/1864893237/User+Report+Play+Video+Submission)  | Send report channel. |
| `playGetContentSlot` | [Get Content Slot (GQL)](/wiki/spaces/CN/pages/798622188)  | Get widgets from <!--start status:PURPLE-->EXPLORE WIDGET<!--end status--> |
| `playToggleChannelReminder` | [Toggle Channel Reminder (GQL)](/wiki/spaces/CN/pages/820707853)  | <!--start status:PURPLE-->EXPLORE WIDGET<!--end status--> Get reminder bell status in widget card (for upcoming channels & tab only) whether is already reminded from user or no. |

## Web Socket Message List

WS contract: [New Chat Room Socket Contract](/wiki/spaces/CN/pages/601065014) 



| **Type** | **Usage** |
| --- | --- |
| `MERCHANT_VOUCHERS` | Get channel newest vouchers. |
| `PRODUCT_TAG_UPDATE` | Get channel newest products. |
| `TOTAL_LIKE` | Get channel newest total likes. |
| `TOTAL_VIEW` | Get channel newest total views. |
| `FREEZE, BANNED` | Get channel live status, if it’s banned or freeze, live channel ended. |
| `PINNED_MESSAGE` | Get channel pinned message. |
| `QUICK_REPLY` | Get channel quick replies for chatting in <!--start status:GREEN-->LIVE<!--end status--> room. |
| `UPDATE_CONFIG_MULTIPLE_LIKE` | Get channel configuration for like (such as background color, icon) |
| `REAL_TIME_NOTIF` | Get real-time-notification for <!--start status:GREEN-->LIVE<!--end status--> channel. Notification contains, if user X join channel room, etc. |
| `USER_WINNER_STATUS` | <!--start status:GREEN-->LIVE<!--end status--> For user that wins a game created by user would get this message to show coach mark about winning information. |

## Action Items

- Add more unit test cases
- Add more analytic test cases coverage
- Simplified some code in view model

## Useful Links

- [Figma](https://www.figma.com/file/W0E7smWmFhavZzBqCwBFh7/%5BPlay%5D-Live-Room-Restructure?node-id=0%3A1&t=Ji3GwelPBMH63J8G-0)
- [Thanos](https://mynakama.tokopedia.com/datatracker/requestdetail/view/222)
- Parent [PRD](https://tokopedia.atlassian.net/wiki/spaces/CN/pages/1918934684/PRD+-+Play+Viewer+Platform)
- [Google ExoPlayer Adaptive Bitrate Analysis](/wiki/spaces/PA/pages/1782782675/Google+ExoPlayer+Adaptive+Bitrate+Analysis)
- [[Android] PLAY x One Click Checkout Integration](/wiki/spaces/PA/pages/2040628552)
- [[Android] PLAY x TokoNOW Integration](/wiki/spaces/PA/pages/1994032302)

## FAQ

<!--start expand:Where can I find channel Id to test?-->
- Live Landscape + Product `tokopedia://play/12665`
- Live Landscape + Pinned Message `tokopedia://play/12668`
- Live Portrait + Product `tokopedia://play/12669`
- Live Portrait + Pinned Message `tokopedia://play/12670`
- VOD Landscape + Product `tokopedia://play/12672`
- VOD Landscape + Pinned Message `tokopedia://play/12677`
- VOD Portrait + Product `tokopedia://play/12678`
- VOD Portrait + Pinned Mesage `tokopedia://play/12679`
- Youtube landscape + Product `tokopedia://play/12680`
- Youtube landscape + Pinned message `tokopedia://play/12681`
- Youtube Portrait + Product `tokopedia://play/12682`
- Youtube Portrait + Pinned Message `tokopedia://play/12683`
- Tokonow: `tokopedia://play/465730`
<!--end expand-->

<!--start expand:Where can I find credential to logged in as tester account?-->
You can ask @play-ep or @android-minion-lance on Slack
<!--end expand-->




[data-colorid=y9shb34cg6]{color:#97a0af} html[data-color-mode=dark] [data-colorid=y9shb34cg6]{color:#505968}[data-colorid=rmoh157qix]{color:#97a0af} html[data-color-mode=dark] [data-colorid=rmoh157qix]{color:#505968}[data-colorid=fpzqzulqpr]{color:#97a0af} html[data-color-mode=dark] [data-colorid=fpzqzulqpr]{color:#505968}[data-colorid=q0cw1x2n3m]{color:#97a0af} html[data-color-mode=dark] [data-colorid=q0cw1x2n3m]{color:#505968}[data-colorid=z1lwoqjb99]{color:#97a0af} html[data-color-mode=dark] [data-colorid=z1lwoqjb99]{color:#505968}