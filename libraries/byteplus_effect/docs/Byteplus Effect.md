---
title: "Byteplus Effect"
labels:
- beautification
- content
- livestream
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence)  |
| Product Manager | [Nelson Adiwira Panata](https://tokopedia.atlassian.net/wiki/people/5d6382ea0a083a0db98ed2bb?ref=confluence) [Fadlim . (Unlicensed)](https://tokopedia.atlassian.net/wiki/people/61e501ac98cd6100703021eb?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 14 July 2023 / <!--start status:BLUE-->MA-3.225<!--end status--> <!--start status:GREEN-->SA-2.155<!--end status-->  |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status--> |
| Module Location | `libraries.byteplus_effect`  |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:14 Jul 2023 (MA-3.225/SA-2.155)-->
###### *First release*

PR : <https://github.com/tokopedia/android-tokopedia-core/pull/32919>
<!--end expand-->

## Overview

`byteplus_effect` is a library to support beautification feature in `play_broadcaster`. It consists of byteplus AAR, byteplus base code, and some process related to beautification.

### Background

Live streaming has become so interesting lately, many people utilizing it for various purpose; product promotion, entertainment, etc. During live streaming, we can’t deny that the host appearance also plays a big role to attract more viewer & engagement. Beautification filter is one of the easiest & practical way to achieve that. Anyone can start live streaming without worrying about their face preparation. With the help of Artificial Intelligence, this beautification feature can manipulate the output camera frame and attach some filters to improve host appearance, with hope it can attract more viewer to join.

### Project Description

Byteplus SDK is a 3rd party library owned by Byteplus, a subsidiary company of Bytedance. It works in the middle of Larix SDK to manipulate the frame output. Byteplus are able to put some face filters and effect if it detects faces within the frame.

As for now, this library handle 2 types of effect:

1. **Face Filter**  
It is a basic effect for your face, e.g. Blur, Smooth, Sharp, and you can combine many of these into filters in 1 frame.
2. **Preset**  
It is a pre-built effect that is ready to be used. You can only implement 1 preset at a time.

 

Both face filter & preset has a capability to adjust the strength of the effect, ranging from 0.0 - 1.0.

<img src="https://docs-android.tokopedia.net/images/docs/res/Screenshot_20230714-154425_testapp.jpg" alt="" />

## Tech Stack

- [Byteplus SDK](https://bytedance.feishu.cn/docs/doccnVdKQbvwaPOxv86Z6dCKUph) : 3rd party library to support beautification filter feature.
- [OpenGL](https://developer.android.com/develop/ui/views/graphics/opengl/about-opengl) : library to support 2D & 3D rendering. Byteplus SDK uses this under the hood.
- On-demand assets : We put Byteplus assets as on-demand to avoid APK increase. It includes the license, model, resources, etc. We use [Retrofit](https://square.github.io/retrofit/) to download `.zip` asset file.

## Flow Diagram

<img src="https://docs-android.tokopedia.net/images/docs/res/image-20230714-052729.png" alt="" />

<img src="https://docs-android.tokopedia.net/images/docs/res/proposed-solution-2-edited-20230207-133219.png" alt="" />

## How-to

To use Byteplus effect tools, don’t forget to add the dependency in your `build.gradle`



```
dependencies {
  implementation projectOrAar(rootProject.ext.libraries.byteplusEffect)
}
```

  
`byteplus_effect` library has 1 main class to perform rendering process called `EffectManager.kt` . To use this class, please register this class in your DI module:



```
@Module
abstract class MyModule {

  abstract fun bindEffectManager(effectManager: EffectManagerImpl): EffectManager
  
}
```

Before initializing / calling Byteplus SDK, you need to make sure you have these assets ready:

1. license
2. model

These assets can be put directly inside our main repo / put it in cloud & download it on-demand. **The recommendation is to put on cloud so the license & model can be renewed on the fly without deploying new APK.** If you put it on the cloud, you need to download the resources first before initializing Byteplus SDK.

When saving the assets, you need to follow this structure:

<img src="https://docs-android.tokopedia.net/images/docs/res/a0d0e52e-f03c-4f89-91da-2ebb3e8b87b9.png" alt="" />

Use `EffectManager` and call these functions:



```
@Inject
private lateinit var effectManager: EffectManager

fun initializeByteplusSDK() {
  // TODO 1: Start thead where rendering is happening
  effectManager.startRenderThread()
  
  // TODO 2: Initialize Byteplus SDK for the first time
  effectManager.init()
  
  // TODO 3: Setup texture for rendering process
  effectManager.setupTexture(surfaceSize.width, surfaceSize.height)
  
  // TODO 4: Setup listener and render the frame within the callback
  effectManager.setRenderListener(surfaceSize.width, surfaceSize.height, object : EffectManager.Listener {
    override fun onRenderFrame(
      destinationTexture: Int,
      textureWidth: Int,
      textureHeight: Int
    ) {
      ...
      
      // TODO 5: Render the frame here
      effectManager.drawFrameBase(
        textureWidth,
        textureHeight,
        surfaceSize.width,
        surfaceSize.height,
        destinationTexture
      )
    }
  })
}

fun release() {
  // TODO 6: call release() when resources is not needed anymore. You can call it on onPause()
  effectManager.release()
}

fun destroy() {
  // TODO 7: stop rendering thread when byteplus is invalid. You can call it on onDestroy()
  effectManager.stopRenderThread()
}
```

To apply some face filters, you need to make sure that you have the resources ready. You can either put the resources directly in your module / put it in cloud as you put your license & model. If you put it on cloud, you need to download it first.

After all the resources is ready, you can call these functions to apply face filters:



```
// Apply & Remove Face Filter
fun applyFaceFilter(faceFilterId: String, value: Float) {
  effectManager.setFaceFilter(faceFilterId, value)
}

fun removeFaceFilter() {
  effectManager.removeFaceFilter()
}



// Apply & Remove Preset
fun applyPreset(presetId: String, value: Float) {
  effectManager.setPreset(presetId, value)
}

fun removePreset() {
  effectManager.removePreset()
}
```

This module also provides some helper classes for handling assets, you can use:

1. **AssetManager**  
It contains some helper function that related to files, like unzip `.zip` file, save & delete directory.
2. **AssetChecker**  
Has some functions to check whether the assets is available or not. it checks model, license, and resources.
3. **AssetHelper**  
Use this class to have access to assets directory path.



---

## Useful Links

- [Byteplus: Integration Guide & Resources](https://bytedance.feishu.cn/docs/doccnVdKQbvwaPOxv86Z6dCKUph)
- [Byteplus: Progress Review](https://bytedance.sg.feishu.cn/docx/doxlg90Ldykja05C0JYeOrzcHwd)
- [Byteplus: Tech Spec](https://bytedance.sg.feishu.cn/docx/doxlgT1GBJ2b8qXAP2vcGKxjdzP)
- [Byteplus: Error Code](https://docs.byteplus.com/en/effects/docs/error-code-table)
- [Byteplus: Internal Assessment](https://docs.google.com/spreadsheets/d/1kPJlN5V0oUN9hSTHTKKYksqB01RaWX5RuvC5TbqBeXM/edit#gid=0)
- [Byteplus: Research Result PPT](https://docs.google.com/presentation/d/15HQGKLULT65oKOJ7CxXup9bgUNGCQuHHnyiGVnZWkbA/edit#slide=id.p)
- [Byteplus: Research Doc](/wiki/spaces/PA/pages/2014151691/BytePlus+Research)
- [Byteplus: Joe’s Log](/wiki/spaces/PA/pages/2014316212/Joe+BytePlus+Logs)
- [Byteplus: Meyta’s Log](https://tokopedia.atlassian.net/wiki/spaces/PA/pages/2151551367/Meyta+s+Note)

## FAQ

<!--start expand:Can we reuse this beautification filter for other purpose outside live streaming? e.g. video recording, video editor, etc?-->
For now we can’t because it’s specific for live streaming use case. But please react out [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) for discussion 🙏
<!--end expand-->

