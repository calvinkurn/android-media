---
title: "MediaPicker: Unified media selection service"
labels:
- imagepicker
- mediaplatform
- media
- picker
---

<!--left header table-->
| **Status**       | <!--start status:GREEN-->RELEASE<!--end status-->                                                                 |
|------------------|-------------------------------------------------------------------------------------------------------------------|
| **Project Lead** | [Muh Isfhani Ghiath](https://tokopedia.atlassian.net/wiki/people/5c5b988f0551865e5bc7986c?ref=confluence)         |
| Product Manager  | [Gilbert .](https://tokopedia.atlassian.net/wiki/people/612c2c400f8ff40068adbfae?ref=confluence)                  |
| Team             | [Minion Jerry]                                                                                                    |
| Release date     | 18 Apr 2022 / <!--start status:GREY-->MA-3.172<!--end status--> <!--start status:GREY-->SA-2.102<!--end status--> |
| Module type      | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                       |
| Product PRD      | [Link](https://tokopedia.atlassian.net/wiki/spaces/CO/pages/1888665726/PRD+Docs+-+Global+Media+Picker)            |
| Module Location  | `features.mediapicker`                                                                                            | `features/media/mediapicker` |

## Table of Contents

<!--toc-->

## Overview

### Background

Universal Media Picker is Tokopedia’s unified and modular Media selection service, part of Media assets upload journey. Through this module, we want to achieve the following objectives:
- Enable video upload journey on iOS and Android
- Unify the experience, looks and feel of media upload journey on all use cases
- Modular and unified modules for Adopters
- Upload and content creation funnel


**Main issues**

Un-unified image and video picker across Tokopedia feature.

### Project Description
Enable video upload journey on iOS and Android
- Through enabling video upload capability on iOS and Android, sellers and buyers can upload video seamlessly through Apps platform.
- With universal Media picker module, user can select and upload video assets from camera or device gallery on iOS and Android.

Unify the experience, looks and feel of media upload journey on all use cases
- Through unified experience, user can navigate and upload media assets seamlessly on all use cases.

Modular and unified modules for Adopters
- Page owners that want to enable upload journey can adopt from unified component and adjust based on their needs.
- As a page owners, modules can be adopted and adjusted according to specific use cases (e.g. image or video upload, with or without editor, etc) without having to be developed from scratch.

Upload and content creation funnel
- We need to be able to track and measure the ease of use for each step in the media upload journey

## Flow Diagram

### MediaPicker general flow
![MediaPicker general flow](https://docs-android.tokopedia.net/images/docs/asd/MediaPicker-main_flow.png "MediaPicker general flow ")


## How-to

### Default


```
val intent = MediaPicker.intent(context) {
     pageSource(PageSource.TOPCHAT)
     modeType(ModeType.IMAGE_ONLY)
     singleSelectionMode()
}

startActivityForResult(intent, REQUST_CODE)
```

### Picker Config (PickerParam)

```
pageType: Int (PageType)
modeType: Int (ModeType)
isMultipleSelection: Boolean
cameraRatio: CameraRatio
maxMediaItem: Int
maxVideoItem: Int
maxVideoFileSize: Long
minVideoDuration: Int
maxVideoDuration: Int
maxImageFileSize: Long
minImageResolution: Int
minStorageThreshold: Long
isIncludeAnimation: Boolean
withEditor: Boolean
withImmersiveEditor: Boolean
pageSource: PageSource
subPageSource: PageSource
includeMedias: List<String>
excludedMedias: List<File>
previewActionText: String
editorParam: EditorParam?
immersiveEditorParam: ConfigurableUniversalEditorParam?
immersiveTrackerData: Map<String, String>

=============================================
PageType:
- PageType.COMMON
- PageType.CAMERA
- PageType.GALLERY

ModeType:
- ModeType.COMMON
- ModeType.IMAGE_ONLY
- ModeType.VIDEO_ONLY

CameraRatio:
- CameraRatio.Square
- CameraRatio.Full

PageSource: (manually added according to usage page)
- PageSource.TopChat
- PageSource.Feed
- etc

EditorParam & ConfigurableUniversalEditorParam please refer to MediaEditor & UniversalEditor for detail.
```

## Action Items

- -

## Useful Links

- -
