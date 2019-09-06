class VersionModel {
    def projectName
    float incrementCount
    def groupId
    def artifactId
    def artifactName
    VersionModel(def projectName, int incrementCount, def groupId, def artifactId, def artifactName){
        this.projectName = projectName
        this.incrementCount = incrementCount
        this.groupId = groupId
        this.artifactId = artifactId
        this.artifactName = artifactName
    }
}
