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
| **Status** | <!--start status:GREEN-->RELEASE<!--end status-->                                                                 |
| --- |-------------------------------------------------------------------------------------------------------------------|
| **Project Lead** | [Calvin](https://tokopedia.atlassian.net/wiki/people/5c6beecd9170244d98d7d0c4)                                    |
| Product Manager | [Gilbert .](https://tokopedia.atlassian.net/wiki/people/612c2c400f8ff40068adbfae?ref=confluence)                  |
| Team | [Minion Jerry]                                                                                                    |
| Release date | 19 Oct 2022 / <!--start status:GREY-->MA-3.198<!--end status--> <!--start status:GREY-->SA-2.128<!--end status--> |
| Module type | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                       |
| Product PRD | [Link](https://tokopedia.atlassian.net/wiki/spaces/CO/pages/2281898008/PRD+-+Immersive+Image+Video+Editor)        |
| Module Location | `features.mediaeditor`                                                                                            | `features/media/medialoader` |

## Table of Contents

<!--toc-->

## Overview

### Background

Throughout H1, we have seen a shift in the general content market trends from users preferring real-time live streams towards shorter length video-on-demand (VOD). Supply-wise, creators prefer to create shorter VODs than live streams. While demand-wise, these VODs have 4x higher order per view conversion compared to live streams. Adding on the fact that our average user watch time is only ~10 seconds, it became a question: How do we help creators create content supplies that are engaging and able to deliver information quickly?

Image & video can potentially solve act as quick delivery of information. Content-quality wise, we aim towards differentiating from TikTok and Shopee Videos who supply all type of videos (news, movie clips, etc). We want to have a supply of product-centric images & videos that answer (1) what is the product & how to use it, (2) inspiration and (3) review & social proof of owning the product. These videos will be created through various funnels namely Product Image & Video Upload, Short Video Creation, Review Image & Video Upload, Manual Story Creation and then shown throughout the main buyer funnel from Home, Search Product Card Sneak-Peek, Shop and PDP. All in a seamless, immersive experience for all types.

To help users in creating engaging content that will be shown everywhere, unifying the Image & Video Editing and making them immersive is necessary to enhance the quality. Although many users are also using external video editor (e.g. CapCut) to edit their videos before uploading, a certain degree of capabilities should be available in Tokopedia Apps to help users who are unaware and not tech-savvy enough but still want a certain degree of enhancement on their media.

### Project Description

A unified media editor is used to edit image & video (video not develop yet), and provide consistency flow and behavior across tokopedia user on editing media  

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

### Other config



```
get editor param:
ratioList(): ArrayList<ImageRatioType>
editorToolsList(): ArrayList<Int>
autoCropRatio(): ImageRatioType
getCustomCtaText(): String

setter editor param:
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

- Add video editor

## Useful Links

- 
