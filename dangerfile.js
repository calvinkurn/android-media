import {danger, warn, markdown} from 'danger'

  
// No PR is too small to include a decription of why you made a change
if (danger.github.pr.body.length < 10) {
  warn('Please include a description of your PR changes.');
}

//Check big PR
var bigPRThreshold = 600;

if (danger.github.pr.changed_files > bigPRThreshold) {
  warn('Big PR (' + ++errorCount + ')');
  markdown('> (' + errorCount + ') : Pull Request size seems relatively large. If Pull Request contains multiple changes, split each into separate PR will helps faster, easier review.');
}


// Danger file, need further checking from EM, to be added more later
// To Do: DeepLinkHandler? NotificationHandler?
var totalDanger = 0

const modifiedBuildGradle = danger.git.modified_files.filter(path => path.includes('build.gradle')).length > 0
if (modifiedBuildGradle) {
  totalDanger++
  warn(":exclamation: Changes were made in build.gradle! Need approval from @ricoharisin")
}
const modifiedDependenciesGradle = danger.git.modified_files.filter(path => path.includes('dependencies.gradle')).length > 0
if (modifiedDependenciesGradle) {
  totalDanger++
  warn(":exclamation: Changes were made in dependencies.gradle! Need approval from @ricoharisin")
}
const addFilesToCore = danger.git.created_files.filter(path => path.startsWith('tkpdcore/')).length > 0
if (addFilesToCore) {
  totalDanger++
  warn(":exclamation: You are not supposed to add any new files to TkpdCore! Pelase consult to your lead!")
}
const modifiedInterceptor = danger.git.modified_files.filter(path => path.includes('interceptor')).length > 0
if (modifiedInterceptor) {
  totalDanger++
  warn(":exclamation: Changes were made in some Interceptor! Please check carefuly!") 
}
const addInterceptor = danger.git.created_files.filter(path => path.includes('interceptor')).length > 0
if (addInterceptor) {
  totalDanger++
  warn(":exclamation: New Interceptor class detected! Please check carefuly!")
}
const modifiedApplication = danger.git.modified_files.filter(path => path.includes('application')).length > 0
if (addInterceptor) {
  totalDanger++
  warn(":exclamation: Changes were made in Application class! Need approval from @ricoharisin")
}

if (totalDanger == 0) {
  markdown("Ok to merge, good job!")
}


  