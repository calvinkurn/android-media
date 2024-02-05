---
title: "UniversalEditor: unified Media editing service"
labels:
- universal-editor
- mediaplatform
- media
- editor
---

<!--left header table-->
| **Status**       | <!--start status:GREEN-->RELEASE<!--end status-->                                                                 |
|------------------|-------------------------------------------------------------------------------------------------------------------|
| **Project Lead** | [Muh Isfhani Ghiath & Calvin]                                                                                     |
| Product Manager  | [Gilbert .](https://tokopedia.atlassian.net/wiki/people/612c2c400f8ff40068adbfae?ref=confluence)                  |
| Team             | [Minion Jerry]                                                                                                    |
| Release date     | 20 Oct 2023 / <!--start status:GREY-->MA-3.242<!--end status--> <!--start status:GREY-->SA-2.172<!--end status--> |
| Module type      | <!--start status:PURPLE-->INTERNAL-LIBRARY<!--end status-->                                                       |
| Product PRD      | [Link](https://tokopedia.atlassian.net/wiki/spaces/CO/pages/2281898008/PRD+-+Immersive+Image+Video+Editor)        |
| Module Location  | `features.editor`                                                                                                 | `features/media/universal-editor` |

## Table of Contents

<!--toc-->

## Overview

### Background

Throughout H1, we have seen a shift in the general content market trends from users preferring real-time live streams towards shorter length video-on-demand (VOD). Supply-wise, creators prefer to create shorter VODs than live streams. While demand-wise, these VODs have 4x higher order per view conversion compared to live streams. Adding on the fact that our average user watch time is only ~10 seconds, it became a question: How do we help creators create content supplies that are engaging and able to deliver information quickly?

Image & video can potentially solve act as quick delivery of information. Content-quality wise, we aim towards differentiating from TikTok and Shopee Videos who supply all type of videos (news, movie clips, etc). We want to have a supply of product-centric images & videos that answer (1) what is the product & how to use it, (2) inspiration and (3) review & social proof of owning the product. These videos will be created through various funnels namely Product Image & Video Upload, Short Video Creation, Review Image & Video Upload, Manual Story Creation and then shown throughout the main buyer funnel from Home, Search Product Card Sneak-Peek, Shop and PDP. All in a seamless, immersive experience for all types.

To help users in creating engaging content that will be shown everywhere, unifying the Image & Video Editing and making them immersive is necessary to enhance the quality. Although many users are also using external video editor (e.g. CapCut) to edit their videos before uploading, a certain degree of capabilities should be available in Tokopedia Apps to help users who are unaware and not tech-savvy enough but still want a certain degree of enhancement on their media.

### Project Description

Create a unified Immersive Editor for Images & Videos to unify the creation experience for a fast and intuitive creation process that enables users to envision what they will create and allow tech to perform quick tech integrations, while providing flexibility to choose the necessary tools. Main User Stories includes:
As a user, I want to edit the media I have captured/selected with various editing tools available
As page owner, I can integrate Immersive Editor to my creation flow, and adjust the tools to my needs
Development in the MVP will be focused on Story Creation, but the aim is to unify the creation flow for all use cases

## Flow Diagram

### UniversalEditor general flow
![UniversalEditor general flow](https://docs-android.tokopedia.net/images/docs/asd/MediaUniversalEditor-main_flow.png "UniversalEditor general flow ")

## How-to

### Default


```
val intent = MediaPicker.intent(context) {
     ...
     withImmersiveEditor {
        setHeaderTitle("My Editor")
        setActionButtonText("Apply")
        setTrackerExtra(mapOf())
     }
     ...
}

=== getting result using normalize function
val result = MediaPicker.result(intent)
println(result.editedPaths.first())
```

### Picker Config (PickerParam)

```
var headerTitle: String
var headerActionButton: String
var trackerExtra: Map<String, String>
var videoFileResultAppendix: String
```

## Action Items

- Add video editor trim feature
- Add image editor tools following old (AEP) editor

## Useful Links

- -
