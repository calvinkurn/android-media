---
Tracker Generator
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Kelvindo Sutan](https://tokopedia.atlassian.net/wiki/people/5ff2a8fe44065f013f93507c?ref=confluence)  |
| Product Manager | [Yehezkiel .](https://tokopedia.atlassian.net/wiki/people/5c94aa7a7792242c8613ad14?ref=confluence)  |
| Team | [Minion Stuart](https://tokopedia.atlassian.net/people/team/eeba862a-bd9d-472c-b901-415b15b1a37e) |
| Release date | - |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status--> |
| Module Location | `libraries.tracking_optimizer` | `libraries.track` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:14 Dec 22-->
Initial Release
<!--end expand-->

## Overview

Tracker Generator is a plugin that help you convert JSON into tracker code. Originally the plugin will generate Tracker Builder.

There is experiment feature that convert JSON into hashMapOf to match up with [Tracker Guidelines](Tracker%20Guidelines.md) documentation.

We offer benefits by using this Tracker Generator.

1. Simple and concise.
2. Less typo when build the code.
3. Improve flow

## How-to

### Download

Download the plugin from [here](https://drive.google.com/file/d/1w0kfjCrsU5DSk4fKNyaEoelAk3Dy0qt6/view?usp=sharing).

### Install Plugin

1. Open Android Studio Preferences > Search “Plugins” > Install Plugin From Disk
2. Then, click OK (You may need to restart Android Studio)

![](res/tracker_generator_illustration_1.png)

### Usage

1. Open a Kotlin Class or Kotlin Object.
2. Tools > Tracker Generator
3. Paste your JSON ResultV5 from MyNakama.
4. Check ✅ Use hashMapOf() builder.
5. Optional - update your function name.
6. Optional - override your event name like from MyNakama.

![](res/tracker_generator_illustration_2.png)

![](res/tracker_generator_illustration_3.png)

### Clean Up

After this you may edit the function with your own style. Then pass mapEvent into tracker library function.

[General Event](General%20Event.md)



```
TrackApp.getInstance().gtm.sendGeneralEvent(mapEvent)
```

[Tracking Queue](Tracking%20Queue.md)



```
trackingQueue.putEETracking(mapEvent)
```

## Useful Links

Here’s some video to demonstrate how to use Tracker Generator

[Screen Recording 2022-12-14 at 10.59.50.mov](https://api.media.atlassian.com/file/13d5e9ba-2734-43f8-bd89-09b9bb676909/artifact/video_1280.mp4/binary?client=f5ed584c-324c-4e33-90a6-b46b8255cc79&collection=contentId-2095776736&max-age=2592000&token=eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJmNWVkNTg0Yy0zMjRjLTRlMzMtOTBhNi1iNDZiODI1NWNjNzkiLCJhY2Nlc3MiOnsidXJuOmZpbGVzdG9yZTpjb2xsZWN0aW9uOmNvbnRlbnRJZC0yMDk1Nzc2NzM2IjpbInJlYWQiXX0sImV4cCI6MTY5MjE2MDQxNiwibmJmIjoxNjkyMTU3NTM2fQ.zy-kXeKjbeVfMPW2QevqLpOyxmFeBnBZu2txWmv3xv8)

Also this Generator will directly help you convert “promotions” key into acceptable tracker library for [Tracking Queue](Tracking%20Queue.md).

Hopefully this generator can speed up the development with minimum issue while creating a tracker.

