---
title: "MediaEditor: Unified Image and Video editor"
labels:
- imageeditor
- videoeditor
- mediaplatform
- media
- editor
---

<!--left header table-->
| **Status**       | <!--start status:GREEN-->RELEASE<!--end status-->                                                                 |
|------------------|-------------------------------------------------------------------------------------------------------------------|
| **Project Lead** | [Calvin](https://tokopedia.atlassian.net/wiki/people/5c6beecd9170244d98d7d0c4)                                    |
| Product Manager  | [Gilbert .](https://tokopedia.atlassian.net/wiki/people/612c2c400f8ff40068adbfae?ref=confluence)                  |
| Team             | [Minion Jerry]                                                                                                    |
| Release date     | 19 Oct 2022 / <!--start status:GREY-->MA-3.198<!--end status--> <!--start status:GREY-->SA-2.128<!--end status--> |
| Module type      | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                       |
| Product PRD      | [Link](https://tokopedia.atlassian.net/wiki/spaces/CO/pages/1888665745/PRD+Docs+-+Global+Media+Editor)        |
| Module Location  | `features.mediaeditor`                                                                                            | `features/media/mediaeditor` |

## Table of Contents

<!--toc-->

## Overview

### Background

Universal Media editor is Tokopedia’s unified and modular Media editing service, part of Media assets upload journey. Through this module, we aim to achieve the following objectives:

- Enable content creation on iOS and Android
- Unify the experience, looks and feel of media editing journey on all use cases
- Modular and unified modules for Adopters
- Upload and content creation funnel
- Base component for Media’s experimentation plan

### Project Description

Enable content creation on iOS and Android
- Through providing editing capability for images and videos on iOS and Android, sellers and buyers can edit video seamlessly without having to open a separate application to edit their media assets
- Enable native editing capabilities on iOS, where basic tools (Crop, Brightness, Contrast, Rotate) are not yet available

Unify the experience, looks and feel of media editing journey on all use cases
- Through unified experience, user can navigate and edit media assets seamlessly on all use cases

Modular and unified modules for Adopters
- Page owners that want to enable editing journey can directly adopt from Media Platform’s unified component and adjust based on their needs
- As page owners, specific editing tools can be activated/deactivated to adjust to their respective use cases (e.g. chat do not need add watermark and remove background feature thus can be turned off)

Upload and content creation funnel
- We need to be able to track and measure the ease of use for each step in the media upload journey

Base component for Media’s experimentation plan
- By centralizing the module to Media, we will be able to own and conduct experiments to improve image and video quality in Tokopedia


## Flow Diagram

### MediaEditor general flow 
![MediaEditor general flow](https://docs-android.tokopedia.net/images/docs/media-docs-img/MediaEditor-main_flow.png "MediaEditor general flow ")

### MediaEditor basic tools flow
![MediaEditor basic tools flow](https://docs-android.tokopedia.net/images/docs/media-docs-img/MediaEditor-basic_tool_flow.png "MediaEditor basic tools flow")

### MediaEditor RemoveBackground flow
![MediaEditor RemoveBackground flow](https://docs-android.tokopedia.net/images/docs/media-docs-img/MediaEditor-remove_bg_flow.png "MediaEditor RemoveBackground flow")

### MediaEditor AddText flow
![MediaEditor AddText flow](https://docs-android.tokopedia.net/images/docs/images_in_zip/MediaEditor-add_text_flow.png "MediaEditor AddText flow")

### MediaEditor AddLogo flow
![MediaEditor AddLogo flow](https://docs-android.tokopedia.net/images/docs/media-docs-img/MediaEditor-add_logo_flow.png "MediaEditor AddLogo flow")


## How-to

### Default



```
val intent = MediaPicker.intent(
            context = this,
            param = {
                withEditor { 
                    this.withAddText()
                }
            }
        )
```

### Editor config



```
Getter:
ratioList(): ArrayList<ImageRatioType>
editorToolsList(): ArrayList<Int>
autoCropRatio(): ImageRatioType
getCustomCtaText(): String

Setter:
withWatermark()
withRemoveBackground()
withAddLogo()
withAddText()
autoCrop1to1()
autoCrop3to4()
autoCrop2to1()
resetRatioList()
ratioListAdd1to1()
ratioListAdd3to4()
ratioListAdd2to1()
setCustomCtaText()

===================================
EditorParam field

ratioList: ImageRatioType (used by crop tools to show allowed ratio)
editorToolsList: EditorToolType (used to show allowed editor tools)
autoCropRatio: ImageRatioType? (will crop media if not null according to given ratio)
customCtaText: String? (customize cta action string if not null)

EditorToolType collection:
EditorToolType.NONE
EditorToolType.BRIGHTNESS (included, cannot be exclude as basic tools)
EditorToolType.CONTRAST (included, cannot be exclude as basic tools)
EditorToolType.CROP (included, cannot be exclude as basic tools)
EditorToolType.ROTATE (included, cannot be exclude as basic tools)
EditorToolType.REMOVE_BACKGROUND
EditorToolType.WATERMARK
EditorToolType.ADD_LOGO
EditorToolType.ADD_TEXT

Ratio collection:
RATIO_1_1
RATIO_3_4
RATIO_2_1
```

## Action Items

- Add video editor (TODO)

## Useful Links

- 
