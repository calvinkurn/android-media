class VersionModel {
    def project
    float version
    def groupId
    def artifactId
    def artifactName
    VersionModel(def project, int version, def groupId, def artifactId, def artifactName){
        this.project = project
        this.version = version
        this.groupId = groupId
        this.artifactId = artifactId
        this.artifactName = artifactName
    }
}
