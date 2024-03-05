---
title: "Creation Common"
labels:
- creation
- content
- common
---


| **Status** | <!--start status:GREEN-->RELEASE<!--end status--> |
| --- | --- |
| Contributors | [Jonathan Darwin](https://tokopedia.atlassian.net/wiki/people/60d02446a01e11006ae4c8f0?ref=confluence) [Furqan .](https://tokopedia.atlassian.net/wiki/people/5c89ce1d43de836baaa6f5ef?ref=confluence)  |
| Product Manager | [Puspacinantya .](https://tokopedia.atlassian.net/wiki/people/6212c45197d313006ba3b24f?ref=confluence)  |
| Team | [Minion Lance](https://tokopedia.atlassian.net/people/team/e1092372-ff41-4537-a48d-4824b575b890) |
| Release date | 15 Dec 2023 / <!--start status:GREY-->MA-3.247<!--end status--> <!--start status:GREY-->SA-2.177<!--end status-->  |
| Module type<br/> | <!--start status:YELLOW-->FEATURE-COMMON<!--end status--> |
| Module Location | `features.creationCommon` | `features/content/creation_common` |

## Table of Contents

<!--toc-->

## Release Notes

<!--start expand:5 Jan 2024 (MA-3.248 / SA-2.178)-->
Entry Point Wording Adjustment, Upload requestId, and Reminder to Open Content from MA (only in SA)

<https://github.com/tokopedia/android-tokopedia-core/pull/36372>
<!--end expand-->

<!--start expand:15 Dec 2023 (MA-3.247 / SA-2.177)-->
First Release

<https://github.com/tokopedia/android-tokopedia-core/pull/36169>
<!--end expand-->

## Overview

This module contains all content-creation-related code that is used outside content scope, e.g. content creation entry point, content upload process & observer, etc. If you have shared code across content creation (gql, util, etc) **for internal purpose**, please consider to put on `content_common` for now.

### Background

As content become more popular, we decided to open the creation entry point & functionality across other pages, e.g. shop page, feed, search, etc. To make it accessible, we put all the shared content creation behavior into this dedicated module, so the other module only depends on specific use case that is available within the module.

### Project Description

Mainly, this module holds 2 functionality:

#### **Creation Entry Point**

image-20240118-075538.png

  
We use bottom sheet to display a list of content creation types that are eligible for user to create. Since this bottom sheet is used across Tokopedia pages, we make it as reusable component. This bottom sheet is attached in Feed Page & Shop Page, and only be accessible if user open their own Feed Page / Shop Page and eligible to create at least 1 type of content.  


Different user has different menus, depends on whitelisting & the level of seller. If user has shop as well, the menus will be shown aggregated from user menus & seller menus.  


#### **Creation Upload**

Because we upload the content sequentially, we have to queue the upload process for all types of content. To centralize the logic, we use this module to store all the uploading process logic. Observing the upload process is also used across Tokopedia pages (Feed & Shop Page). It should use the same observer instance to make the progress sync across pages, therefore we create it as reusable component.  
  
As of now, we have 3 types of content creation that involve uploading process:

- Post
- Shorts
- Stories

Each of these has both similarities & differences, please refer to the table below



|  | **Post** | **Shorts** | **Stories** |
| --- | --- | --- | --- |
| Display upload indicator in Notification | ✅ (**Post** is in progress) |
| Display upload indicator in Feed Page | ✅ |
| Support queue upload | ✅ |
| Has error logger | ✅ |
| Upload multiple media | ✅ | ❌ |
| Main PIC | CVP & Media | CCP & Media |
| Progress count calculation | Based on how many media that has been uploaded successfully. We make the calculation by our own. | Based on how many chunks of media that has been sent to server. We use the function provided by Media team. |
| Type of content | Image (some older versions might have video as well, but now it has been removed) | Video | Image & Video |

The main parent package that stores all the uploading process is `upload (com.tokopedia.creation.common.upload)` . This package is not only contains basic feature things like analytic, const, domain, data, di, but also has some other important components that are described in this table below:



| **Component** | **Usage** |
| --- | --- |
| Activity (`ContentCreationPostUploadActivity.kt`) | Act as an empty activity that determine the next action when user tries to open the uploaded content & also hitting tracker. It has some validation e.g. whether the user is from SA or not, has MA installed or not, etc. |
| Dialog (`ContentInstallMainAppDialog.kt` ) | Used to inform user to download MA so they can see the content. Only appear if user uploads the content from MA, tries to open it, and has no MA installed. |
| Manager | Used to store the main logic & flow for uploading content. Please extend `CreationUploadManager.kt` if you want to create another type of content upload. |
| Notification | Used to display notification that includes the status of upload (on progress, success, failed). Please extend `CreationUploadNotificationManager.kt` if you want to create a new upload notification. |
| Receiver | Used to receive all the events from Notification. For now, it’s used to handle when user wants to retry & cancel the content upload. |
| Worker | Used as the main service to cater all types of content upload. It handles the queueing process, saves the uploading process to local DB, and keeps the uploading process running even when the app is closed. |

## Tech Stack

- [**Room**](https://developer.android.com/training/data-storage/room) : Part of Jetpack Libraries that helps us to store persistence data on local device. It’s used for handling content upload queueing.
- [**Work Manager**](https://developer.android.com/topic/libraries/architecture/workmanager) **:** Part of Jetpack Libraries that helps us to run long-running services without UI. It’s used for uploading contents (post, shorts, stories).
- [**Jetpack Compose**](https://developer.android.com/jetpack/compose) : Part of Jetpack Libraries that helps us to construct UI with declarative approach. With Jetpack Compose, we don’t need to create XML layout file anymore, instead all the UI is written inside kotlin file.
- [**Flow**](https://developer.android.com/kotlin/flow) : Part of coroutine library that helps us to observe data in coroutine way. It’s used for observing upload progress every time room database is updated.
- [**Notification**](https://developer.android.com/develop/ui/views/notifications) : It’s used to display content upload progress & status (on progress, success, or failed).

## Flow Diagram

Creation Common - Upload Flow-20240105-064204.png

Creation Common - Creation Entry Point Bottom Sheet (1)-20240105-083630.png

## How-to

Before you start using this module functionality, please add our module in your gradle dependency



```
dependencies {
  ...
  implementation projectOrAar(rootProject.ext.features.creationCommon)
  ...
}
```

### Upload & Observe Content Progress

1. Add DI Component dependency



```
@Component(
    modules = [
        ...
    ],
    dependencies = [
        ...
        CreationUploaderComponent::class,
    ]
)
@YourComponentScope
interface YourComponent {
    fun inject(activity: YourActivity)
}
```
2. Provide `CreationUploaderComponent` component using `CreationUploaderComponentProvider`



```
fun inject() {
    DaggerYourComponent.builder()
        .creationUploaderComponent(CreationUploaderComponentProvider.get(this))
        .build()
        .inject(this)
}
```

#### Upload Content

1. You can inject & use `CreationUploader.kt` to insert *to-be-uploaded* content data to upload queue.



```
class YourViewModel @Inject constructor(
  private val creationUploader: CreationUploader
) : ViewModel {
  
  private fun upload() {
    val uploadData = creationUploader.buildForShorts(
      ...
    )
    
    creationUploader.upload(uploadData)
  }
}
```


Please use `buildForXXX` to generate upload data. If your type of content is not exists, please create a new one within `CreationUploaderImpl.kt`


#### Observe Upload Content Progress

1. You can inject & use `CreationUploader.kt` to observe the current uploaded content



```
class YourFragment : Fragment() {
    
    @Inject
    lateinit var creationUploader: CreationUploader
    
    private fun observeUpload() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                creationUploader.observe().collect { uploadResult ->
                    when (uploadResult) {
                        is CreationResult.Empty -> {}
                        is CreationResult.Upload -> {}
                        is CreationResult.OtherProcess -> {}
                        is CreationResult.Success -> {}
                        is CreationResult.Error -> {}
                        else -> {}
                    }
                }
            }
        }
    }
}
```

#### Retry Upload

1. Use `retry()` method to retry uploading the top content in the queue



```
creationUploader.retry(uploadData.notificationIdAfterUpload)
```

#### Remove From Queue

1. If you want to remove the top queue, use `deleteTopQueue`



```
creationUploader.deleteTopQueue()
```
2. If you want to remove the queue using queueId, use `deleteFromQueue(queueId)`



```
creationUploader.deleteFromQueue(uploadData.queueId)
```

### Content Creation Entry Point Bottom Sheet

1. Show `ContentCreationBottomSheet` by using `getOrCreateFragment()`



```
fun showContentCreationBottomSheet() {
    ContentCreationBottomSheet
        .getOrCreateFragment(childFragmentManager, requireActivity().classLoader)
        .show(childFragmentManager)
}
```
2. Listen when fragment is on attached to parent & set data if necessary



```
override fun onCreate(savedInstanceState: Bundle?) {
    /** Put this before super.onCreate() */
    childFragmentManager.addFragmentOnAttachListener { _, fragment ->
        when (fragment) {
            is ContentCreationBottomSheet -> {
                /** None of these are mandatory, please use as needed */
                fragment.widgetSource = ContentCreationEntryPointSource.Feed
                fragment.shouldShowPerformanceAction = false
                fragment.analytics = contentCreationAnalytics
                fragment.creationConfig = creationConfig
                fragment.listener = object : ContentCreationBottomSheet.Listener {
                    override fun onCreationNextClicked(data: ContentCreationItemModel) {
                        ...
                    }
                }
            }
        }
    }
    
    super.onCreate(savedInstanceState)
}
```

Please refer to table below for more details about ContentCreationBottomSheet data:



| **Name** | **Description** | **Value** |
| --- | --- | --- |
| widgetSource | Used as parameter for getting bottom sheet data | `ContentCreationEntryPointSource.kt` |
| shouldShowPerformanceAction | Used for displaying `Lihat Performa`, an entry point to Content Performance Page (Webview). For now, it's only used in Shop Page. | true / false |
| analytics | Used for attaching analytics within the bottom sheet | `ContentCreationAnalytics.kt` |
| creationConfig | If you don’t provide this data, the bottom sheet will fetch the config immediately after you open it. If you already have the config, we recommend you to fill this data for better performance | `ContentCreationConfigModel.kt` |
| listener | Used to listen to bottom sheet callback. | `ContentCreationBottomSheet.Listener` |



---

## Useful Links

- [PRD : Content Creation Entry Point Bottom Sheet & Upload (was made along with stories creation feature)](/wiki/spaces/CN/pages/2306803456)
- [Figma : Content Creation Entry Point Bottom Sheet](https://www.figma.com/file/ONt2h5of5NUtORNnLSFXPB/%5BCreation%5D-Story---Mobile?node-id=20666%3A37595&mode=dev)
- [Figma : Content Creation Upload Progress on Feed Page](https://www.figma.com/file/ONt2h5of5NUtORNnLSFXPB/%5BCreation%5D-Story---Mobile?node-id=20670%3A43003&mode=dev)
- [Figma : Content Creation Upload Progress on Notification](https://www.figma.com/file/ONt2h5of5NUtORNnLSFXPB/%5BCreation%5D-Story---Mobile?node-id=20694%3A67511&mode=dev)

## FAQ

<!--start expand:Can I use the upload service to upload my media outside content?-->
For now, Creation Uploader Service is only for content use case and don’t use this outside content scope. If you have anything to discuss, please contact `android-minionlance-dev` on Slack.
<!--end expand-->

