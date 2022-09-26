# Make it more obvious that a PR is a work in progress and shouldn't be merged yet
warn("PR is classed as Work in Progress") if github.pr_title.include? "[WIP]"

# Give a warning if the PR description is empty
desc = github.pr_body.split('Phab Link')[0].gsub(/\s+/, "")
warn("@#{github.pr_author} your Pull Request description is empty. Please provide PR description so we understand what do you want to merge") if desc.length <= 15

# Give a warning if the PR Jira Link is empty
warn("@#{github.pr_author} please provide a JIRA ticket link or type /create jira @sonoda-bot will help you to create the Jira Ticket") if github.pr_body.include? "https://phab.tokopedia.com/XXXXX"

# Give a warning when a PR is over expected size
warn("This PR is quite a big one! Try splitting this into separate tasks next time 🙂") if git.lines_of_code > 2000

# Rollence Report
if File.exists?("rollence_report.txt")
   File.open("rollence_report.txt") do |f|
      markdown(f.read)
   end
end

# Darkmode Report
if File.exists?("fileOutputReport.txt")
   File.open("fileOutputReport.txt") do |f|
      markdown(f.read)
   end
end

# Duplicate Query Report
if File.exists?("duplicate_query_report.txt")
   File.open("duplicate_query_report.txt") do |f|
      markdown(f.read)
   end
end

# AndroidLint
android_lint.report_file = "report-result.xml"
android_lint.correction_file = "lint-correction.json"
android_lint.skip_gradle_task = true
android_lint.severity = "Warning"
android_lint.filtering = true
android_lint.filtering_lines = true
android_lint.lint(inline_mode: true)
#
# Kotlin Detekt
kotlin_detekt.filtering = true
kotlin_detekt.filtering_lines = true
kotlin_detekt.gradle_task = "detektCheck"
kotlin_detekt.report_file = "detekt_result.xml"
kotlin_detekt.detekt(inline_mode: true)

# Ktlin
ktlint.skip_lint = true
ktlint.filtering = true
ktlint.filtering_lines = true
ktlint.correction = true
ktlint.report_file = 'ktlint-report.json'
ktlint.lint(inline_mode: true)
