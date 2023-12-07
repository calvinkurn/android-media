---
title: "Live Broadcaster"
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Meyta Taliti](https://tokopedia.atlassian.net/wiki/people/5c8f676b8c3aae2d15113a7c?ref=confluence) [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence)  |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence) [Fadlim . (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/61e501ac98cd6100703021eb?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release Version | <!--start status:BLUE-->MA-3.147<!--end status--> <!--start status:GREEN-->SA-2.77<!--end status-->  |
| Module type | <!--start status:BLUE-->SUB-FEATURE<!--end status--> |
| Module Location | `features.liveBroadcaster` | `features/media/live-broadcaster` |
| PRD | - |

## Table of Contents

<!--toc-->

## Release Notes (max 5 latest release notes)

<!--start expand:12 Oct 2021 (MA-3.230/SA-2.160)-->
[AN-52420](https://tokopedia.atlassian.net/browse/AN-52420)
 -
 Getting issue details...

STATUS
<!--end expand-->

<!--start expand:12 Oct 2021 (MA-3.225/SA-2.155)-->
[CCP-612](https://tokopedia.atlassian.net/browse/CCP-612)
 -
 Getting issue details...

STATUS
<!--end expand-->

## Overview

**Larix Broadcaster** is a helper library for `play_broadcaster` module to perform rendering on both livestreamer device & viewer side.

### Background

Previously, all the live streaming related logic is handle by Media team, that’s why this module is exists so Media team can maintain the code without interfering play\_broadcaster code. But since most of the maintenances are done in Content team, we decided to change the code owner of the module to Minion Lance completely.

### Project Description

This module has 1 main class called `BroadcastManager` that handles:

1. Rendering process on live streamer's device
2. Pushing live streaming frame to viewer side
3. Beautification rendering logic
4. Create connection to server when live streamer is ready to start live stream.
5. Adaptive bitrate logic depends on live streamer’s internet connection
6. Live stream metric logger

The rest is just a helper class to support `BroadcastManager`

## Tech Stack

- [**Larix**](https://softvelum.com/larix/android_sdk/) : a 3rd library used to push camera output frame to server. Larix already accomodates all the things that we need for livestreaming and it’s free.
- [**Byteplus SDK**](https://bytedance.feishu.cn/docs/doccnVdKQbvwaPOxv86Z6dCKUph) : 3rd party library to support beautification filter feature.

## How-to

1. Init broadcaster manager before performing any operations by calling `init` method.



```
broadcastManager.init(context, handler)
```
2. Add live streaming state listener by calling `addListener` method.



```
broadcastManager.addListener(broadcastListener)
```
3. To enable broadcast statistic logger, call `enableStatistic` method.



```
broadcastManager.enableStatistic(interval)
```
4. Setup thread for beautification rendering by calling `setupThread` method. 



```
broadcastManager.setupThread(isWithByteplus)
```
5. Setup live streaming configuration by calling `setupConfig` method.



```
broadcasterManager.setupConfig(audioRate, videoRate, videoFps)
```
6. To start rendering process, call `create` method. 



```
broadcasterManager.create(surfaceHolder, surfaceSize, withByteplus)
```
7. Your surface view can be updated anytime, therefore call `updateSurfaceSize` when `SurfaceView.surfaceChanged` callback is called.



```
broadcastManager.updateSurfaceSize(surfaceSize)
```
8. To start live streaming, call `start` method.



```
 broadcastManager.start(rtmpUrl)
```
9. When you want to retry the live streaming connection, call `retry` method.



```
broadcastManager.retry()
```
10. To switch camera (front & back camera), call `flip` method.



```
broadcastManager.flip()
```
11. To apply/remove beautification filter (face filter & preset), call these functions.



```
// Face Filter
broadcastManager.setFaceFilter(faceFilterId, value)
broadcastManager.removeFaceFilter()

// Preset
broadcastManager.setPreset(presetId, value)
broadcastManager.removePreset()
```
12. To stop live streaming, call `stop` method.



```
broadcastManager.stop()
```
13. Release the broadcaster resources when it’s not needed by calling `release` method.



```
broadcastManager.release()
```
14. To clean up the broadcaster completely, call `destroy` method.



```
broadcastManager.destroy()
```



---

## Useful Links

- [Byteplus Effect Library Documentation](/wiki/spaces/PA/pages/2307753237/Byteplus+Effect)

## FAQ

<!--start expand:What kind of surface is used for live streaming?-->
Currently we are using `SurfaceView`
<!--end expand-->

<!--start expand:How to avoid using any byteplus SDK?-->
send `false` value in every `withByteplus` argument, for now it’s on `setupThread` & `create` method.od.
<!--end expand-->

